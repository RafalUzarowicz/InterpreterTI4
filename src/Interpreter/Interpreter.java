package Interpreter;

import Board.Board;
import Exceptions.InterpreterException;
import Interpreter.Environment.BlockContext;
import Interpreter.Environment.CallContext;
import Interpreter.Environment.Environment;
import Utilities.Dictionary;
import Utilities.InterpreterUtils;
import Utilities.OStream;
import Utilities.ProgramTree.*;
import Utilities.ProgramTree.BoardChange.*;
import Utilities.ProgramTree.ConditionExpresion.*;
import Utilities.ProgramTree.ConditionExpresion.Operators.*;
import Utilities.ProgramTree.Statements.*;
import Utilities.ProgramTree.Value.BoardStateCheck.ActivationCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.HexStateCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.PlanetStateCheck;
import Utilities.ProgramTree.Value.BoardStateCheck.PlayerStateCheck;
import Utilities.ProgramTree.Value.FunctionCallValue;
import Utilities.ProgramTree.Value.Literals.*;
import Utilities.ProgramTree.Value.Value;
import Utilities.ProgramTree.Value.VariableValue;
import Utilities.ProgramTree.Variables.*;

import java.util.ArrayList;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * Class implementing IVisitor interface - it executes program based on program tree.
 */
public class Interpreter implements IVisitor{
    private final Board board;
    private final Program program;
    private final Environment environment;
    private final OStream outStream;

    public Board getBoard() {
        return board;
    }

    public Program getProgram() {
        return program;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public OStream getOutStream() {
        return outStream;
    }

    public Interpreter(Program program, Board board, OStream outStream){
        this.program = program;
        this.board = board;
        this.environment = new Environment();
        this.outStream = outStream;
    }

    public void execute() throws Exception {
        program.accept(this);
    }

    // Program tree
    @Override public void visit(Program program) throws Exception {
        Function mainFun = program.getFunction("main");
        if(mainFun == null){
            throw new InterpreterException("No main function.");
        }
        else if(mainFun.getParameters().getParametersNumber() != 0){
            throw new InterpreterException("Main function can't take any parameters.");
        }
        else{
            mainFun.accept(this);
        }
        outStream.print("\nMain function has returned "+environment.peekValue().getValue()+".");
    }

    @Override public void visit(Function function) throws Exception {
        environment.pushCallContext();
        CallContext callContext = environment.peekCallContext();
        callContext.pushBlockContext();
        function.getParameters().accept(this);
        for (int i = 0; i < function.getParameters().getParameters().size(); ++i) {
            environment.peekCallContext().setVariableValue(environment.getCurrentStatementLine(), function.getParameters().getParameters().get(i).getName(), environment.popValue().getValue());
        }
        function.getBlock().accept(this);
        if(!environment.hasReturned()){
            throw new InterpreterException(function.getPosition().getLine(), "Function "+function.getIdentifier()+" has not returned any value.");
        }
        callContext.popBlockContext();
        environment.popCallContext();
    }

    @Override public void visit(Block block) throws Exception {
        environment.peekCallContext().pushBlockContext();
        for (var stm: block.getStatements()) {
            if(environment.hasReturned() || environment.hasBroken() || environment.hasContinued()){
                break;
            }
            environment.setCurrentStatement(stm);
            InterpreterUtils.setLine(environment.getCurrentStatementLine());
            stm.accept(this);
        }
        environment.peekCallContext().popBlockContext();
    }

    @Override public void visit(Parameters parameters) throws InterpreterException {
        BlockContext blockContext = environment.peekCallContext().peekBlockContext();
        for (var param: parameters.getParameters()) {
            if(!blockContext.contains(param.getName())){
                blockContext.setVariable(param.getName(), param);
            }else{
                throw new InterpreterException(environment.getCurrentStatementLine(), "Parameter "+param.getName()+" already defined.");
            }
        }
    }

    @Override public void visit(Arguments arguments) throws Exception {
        for (int i = arguments.getArguments().size() - 1; i >= 0; --i) {
            arguments.getArguments().get(i).accept(this);
        }
    }

    // Variables
    @Override public void visit(ArrayVariable arrayVariable){
        // This visitor doesn't need to visit this node.
    }
    @Override public void visit(BoolVariable boolVariable){
        // This visitor doesn't need to visit this node.
    }
    @Override public void visit(ColorVariable colorVariable){
        // This visitor doesn't need to visit this node.
    }
    @Override public void visit(HexVariable hexVariable){
        // This visitor doesn't need to visit this node.
    }
    @Override public void visit(IntVariable intVariable){
        // This visitor doesn't need to visit this node.
    }
    @Override public void visit(PlanetVariable planetVariable){
        // This visitor doesn't need to visit this node.
    }
    @Override public void visit(StringVariable stringVariable){
        // This visitor doesn't need to visit this node.
    }
    @Override public void visit(UnitVariable unitVariable){
        // This visitor doesn't need to visit this node.
    }
    @Override public void visit(Variable variable){
        // This visitor doesn't need to visit this node.
    }
    @Override public void visit(VarVariable varVariable){
        // This visitor doesn't need to visit this node.
    }

    // Value
    @Override public void visit(VariableValue variableValue) throws Exception {
        Variable variable = environment.peekCallContext().getVariable(variableValue.getName());
        if(variable != null){
            if(variableValue.getIndex() != null){
                variableValue.getIndex().accept(this);
                if(variable.getClass().equals(ArrayVariable.class)){
                    IntLiteral index = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
                    ((ArrayVariable) variable).getValue(Integer.parseInt(index.getValue())).accept(this);
                }else{
                    throw new InterpreterException(environment.getCurrentStatementLine(), "Variable is not array: "+variableValue.getName());
                }
            }else{
                environment.pushValue(InterpreterUtils.literalCast(new Literal(variable.getValue()), variable));
            }
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Unknown variable: "+variableValue.getName());
        }
    }

    @Override public void visit(Value value){
        // This visitor doesn't need to visit this node.
    }

    @Override public void visit(FunctionCallValue functionCallValue) throws Exception {
        Function function = program.getFunction(functionCallValue.getIdentifier());
        if(function == null){
            throw new InterpreterException(environment.getCurrentStatementLine(), "Unknown function call.");
        }
        if(function.getParameters().getParameters().size() != functionCallValue.getArguments().getSize()){
            throw new InterpreterException(environment.getCurrentStatementLine(), "Wrong number of arguments.");
        }
        functionCallValue.getArguments().accept(this);
        function.accept(this);
        if(environment.hasReturned()){
            environment.setHasReturned(false);
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Function didn't return a value.");
        }
    }

    // Literals
    @Override public void visit(BoolLiteral boolLiteral) throws InterpreterException {
        environment.pushValue(InterpreterUtils.literalCast(boolLiteral, new BoolVariable("")));
    }

    @Override public void visit(ColorLiteral colorLiteral) throws InterpreterException {
        environment.pushValue(InterpreterUtils.literalCast(colorLiteral, new ColorVariable("")));
    }

    @Override public void visit(HexLiteral hexLiteral) throws InterpreterException {
        environment.pushValue(InterpreterUtils.literalCast(hexLiteral, new HexVariable("")));
    }

    @Override public void visit(IntLiteral intLiteral) throws InterpreterException {
        environment.pushValue(InterpreterUtils.literalCast(intLiteral, new IntVariable("")));
    }

    @Override public void visit(Literal literal){
        // This visitor doesn't need to visit this node.
    }

    @Override public void visit(PlanetLiteral planetLiteral) throws InterpreterException {
        environment.pushValue(InterpreterUtils.literalCast(planetLiteral, new PlanetVariable("")));
    }

    @Override public void visit(StringLiteral stringLiteral) throws InterpreterException {
        environment.pushValue(InterpreterUtils.literalCast(stringLiteral, new StringVariable("")));
    }

    @Override public void visit(UnitLiteral unitLiteral) throws InterpreterException {
        environment.pushValue(InterpreterUtils.literalCast(unitLiteral, new UnitVariable("")));
    }

    // BoardStateCheck
    @Override public void visit(ActivationCheck activationCheck) throws Exception {
        activationCheck.getPlayer().accept(this);
        ColorLiteral player = (ColorLiteral) InterpreterUtils.literalCast(environment.popValue(), new ColorVariable(""));
        activationCheck.getHex().accept(this);
        HexLiteral hex = (HexLiteral) InterpreterUtils.literalCast(environment.popValue(), new HexVariable(""));

        boolean isActivated = board.isActivated(Integer.parseInt(hex.getValue().substring(1)), Dictionary.PlayerColors.valueOf(player.getValue()));

        environment.pushValue(new BoolLiteral(Boolean.toString(isActivated)));
    }

    @Override public void visit(HexStateCheck hexStateCheck) throws Exception {
        hexStateCheck.getHex().accept(this);
        HexLiteral hex = (HexLiteral) InterpreterUtils.literalCast(environment.popValue(), new HexVariable(""));
        hexStateCheck.getUnit().accept(this);
        UnitLiteral unit = (UnitLiteral) InterpreterUtils.literalCast(environment.popValue(), new UnitVariable(""));

        int count = board.getHexState(Integer.parseInt(hex.getValue().substring(1)), Dictionary.SpaceUnits.valueOf(unit.getValue()));

        environment.pushValue(new IntLiteral(Integer.toString(count)));
    }

    @Override public void visit(PlanetStateCheck planetStateCheck) throws Exception {
        planetStateCheck.getPlanet().accept(this);
        PlanetLiteral planet = (PlanetLiteral) InterpreterUtils.literalCast(environment.popValue(), new PlanetVariable(""));
        planetStateCheck.getUnit().accept(this);
        UnitLiteral unit = (UnitLiteral) InterpreterUtils.literalCast(environment.popValue(), new UnitVariable(""));

        int count = board.getPlanetState(Integer.parseInt(planet.getValue().substring(1)), Dictionary.LandUnits.valueOf(unit.getValue()));

        environment.pushValue(new IntLiteral(Integer.toString(count)));
    }

    @Override public void visit(PlayerStateCheck playerStateCheck) throws Exception {
        playerStateCheck.getPlayer().accept(this);
        ColorLiteral player = (ColorLiteral) InterpreterUtils.literalCast(environment.popValue(), new ColorVariable(""));
        playerStateCheck.getPlace().accept(this);
        Literal place;
        if(environment.peekValue().getClass().equals(HexLiteral.class)){
            place = InterpreterUtils.literalCast(environment.popValue(), new HexVariable(""));
        }else if(environment.peekValue().getClass().equals(PlanetLiteral.class)){
            place = InterpreterUtils.literalCast(environment.popValue(), new PlanetVariable(""));
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Hex or planet value expected.");
        }
        playerStateCheck.getUnit().accept(this);
        UnitLiteral unit = (UnitLiteral) InterpreterUtils.literalCast(environment.popValue(), new UnitVariable(""));

        int count;
        if(place.getClass().equals(HexLiteral.class)){
            count = board.getPlayerHexUnitNumber(Integer.parseInt(place.getValue().substring(1)),Dictionary.PlayerColors.valueOf(player.getValue()), Dictionary.SpaceUnits.valueOf(unit.getValue()));
        }else if(place.getClass().equals(PlanetLiteral.class)){
            count = board.getPlayerPlanetUnitNumber(Integer.parseInt(place.getValue().substring(1)),Dictionary.PlayerColors.valueOf(player.getValue()), Dictionary.LandUnits.valueOf(unit.getValue()));
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Hex or planet value expected.");
        }

        environment.pushValue(new IntLiteral(Integer.toString(count)));
    }

    // Statements
    @Override public void visit(ArrayDeclaration arrayDeclaration) throws Exception {
        ArrayVariable arrayVariable = arrayDeclaration.getType();
        Variable variable = arrayVariable.getVariable();

        environment.peekCallContext().peekBlockContext().setVariable(arrayVariable.getName(), arrayVariable);
        arrayVariable = (ArrayVariable) environment.peekCallContext().getVariable(arrayVariable.getName());

        if(arrayDeclaration.getValues().size() != arrayVariable.size()){
            throw new InterpreterException(arrayDeclaration.getPosition().getLine(), "Array elements number mismatch.");
        }

        int i = 0;
        for (var value: arrayDeclaration.getValues() ) {
            value.accept(this);
            Literal literal = InterpreterUtils.literalCast(environment.popValue(), variable);
            arrayVariable.setValue(i, literal);
            ++i;
        }
    }

    @Override public void visit(Assignment assignment) throws Exception {
        CallContext callContext = environment.peekCallContext();

        Variable variable = callContext.getVariable(assignment.getIdentifier());

        Expression orCondition = assignment.getValue();

        orCondition.accept(this);

        Literal literal = environment.popValue();

        if(ArrayVariable.class.equals(variable.getClass())){
            Literal casted = InterpreterUtils.literalCast(literal, ((ArrayVariable) variable).getVariable());
            int index = Integer.parseInt(assignment.getIndex());
            ((ArrayVariable) variable).setValue(index, casted);
        }else{
            Literal casted = InterpreterUtils.literalCast(literal, variable);
            variable.setValue(casted.getValue());
        }
    }

    @Override public void visit(Break breakStm){
        environment.setHasBroken(true);
    }

    @Override public void visit(Conditional conditional) throws Exception {
        conditional.getCondition().accept(this);
        BoolLiteral literal = (BoolLiteral) InterpreterUtils.literalCast(environment.popValue(), new BoolVariable(""));

        if(literal.getValue().equals("true")){
            conditional.getIfBlock().accept(this);
        }else if( conditional.getElseBlock() != null ){
            conditional.getElseBlock().accept(this);
        }
    }

    @Override public void visit(Continue continueStm){
        environment.setHasContinued(true);
    }

    @Override public void visit(FunctionCall functionCallStm) throws Exception {
        Function function = program.getFunction(functionCallStm.getIdentifier());
        if(function == null){
            throw new InterpreterException(environment.getCurrentStatementLine(), "Unknown function call.");
        }
        if(function.getParameters().getParameters().size() != functionCallStm.getArguments().getSize()){
            throw new InterpreterException(environment.getCurrentStatementLine(), "Wrong number of arguments.");
        }
        functionCallStm.getArguments().accept(this);
        function.accept(this);
        if(environment.hasReturned()){
            environment.setHasReturned(false);
            environment.popValue();
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Function didn't return a value.");
        }
    }

    @Override public void visit(Loop loop) throws Exception {
        environment.setHasContinued(false);
        environment.setHasBroken(false);
        ArrayVariable arrayVariable = (ArrayVariable) environment.peekCallContext().getVariable(loop.getArrayIdentifier());

        if(!loop.getVariable().getClass().equals(VarVariable.class) && !loop.getVariable().getClass().equals(arrayVariable.getVariable().getClass())){
            throw new InterpreterException(loop.getPosition().getLine(), "Types doesn't match inside loop");
        }
        environment.peekCallContext().pushBlockContext();
        if(loop.getVariable().getClass().equals(VarVariable.class)){
            environment.peekCallContext().peekBlockContext().setVariable(loop.getVariable().getName(), loop.getVariable());
        }else{
            environment.peekCallContext().peekBlockContext().setVariable(loop.getVariable().getName(), loop.getVariable());
        }
        ArrayVariable array = (ArrayVariable) environment.peekCallContext().getVariable(loop.getArrayIdentifier());
        for(int i = 0; i<arrayVariable.size(); ++i){
            array.getValue(i).accept(this);
            environment.peekCallContext().setVariableValue(environment.getCurrentStatementLine(), loop.getVariable().getName(), environment.popValue().getValue());
            if(!environment.hasContinued()){
                loop.getBlock().accept(this);
            }
            environment.setHasContinued(false);
            String val = environment.peekCallContext().getVariable(loop.getVariable().getName()).getValue();
            if(val != null){
                array.setValue(i, InterpreterUtils.literalCast(new Literal(val), array.getVariable()));
            }
            if(environment.hasBroken()){
                break;
            }
        }
        environment.peekCallContext().popBlockContext();
        environment.setHasContinued(false);
        environment.setHasBroken(false);
    }

    @Override public void visit(Print print) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        Literal literal;
        for( var orCondition: print.getConditionExpressions()){

            orCondition.accept(this);

            literal = environment.popValue();

            stringBuilder.append(literal.getValue());
        }
        outStream.print(stringBuilder.toString());
    }

    @Override public void visit(Return returnStm) throws Exception {
        returnStm.getReturnValue().accept(this);
        environment.setHasReturned(true);
    }

    @Override public void visit(VariableDeclaration variableDeclaration) throws Exception {
        Variable variable = variableDeclaration.getVariable();

        variableDeclaration.getValue().accept(this);

        Literal literal = InterpreterUtils.literalCast(environment.popValue(), variable);

        environment.peekCallContext().peekBlockContext().setVariable(variable.getName(), variable);

        Variable variable1 = environment.peekCallContext().peekBlockContext().getVariable(variable.getName());
        variable1.setValue(literal.getValue());
    }

    // ConditionExpression
    @Override public void visit(OrCondition orCondition) throws Exception {
        ArrayList<Expression> andConditions = orCondition.getAndConditions();
        andConditions.get(0).accept(this);
        OrOperator orOperator = new OrOperator();
        for(int i = 1; i<andConditions.size(); ++i){
            andConditions.get(i).accept(this);
            orOperator.accept(this);
        }
    }

    @Override public void visit(AndCondition andCondition) throws Exception {
        ArrayList<Expression> equalityConditions = andCondition.getEqualityCondition();
        equalityConditions.get(0).accept(this);
        AndOperator andOperator = new AndOperator();
        for(int i = 1; i<equalityConditions.size(); ++i){
            equalityConditions.get(i).accept(this);
            andOperator.accept(this);
        }
    }

    @Override public void visit(EqualityCondition equalityCondition) throws Exception {
        ArrayList<Expression> relationalConditions = equalityCondition.getRelationalConditions();
        ArrayList<Operator> operators = equalityCondition.getOperators();
        relationalConditions.get(0).accept(this);
        for(int i = 1; i<relationalConditions.size(); ++i){
            relationalConditions.get(i).accept(this);
            operators.get(i-1).accept(this);
        }
    }

    @Override public void visit(RelationalCondition relationalCondition) throws Exception {
        ArrayList<Expression> addExpressions = relationalCondition.getAddExpressions();
        ArrayList<Operator> operators = relationalCondition.getOperators();
        addExpressions.get(0).accept(this);
        for(int i = 1; i<addExpressions.size(); ++i){
            addExpressions.get(i).accept(this);
            operators.get(i-1).accept(this);
        }
    }

    @Override public void visit(AddExpression addExpression) throws Exception {
        ArrayList<Expression> multiplyExpressions = addExpression.getMultiplyExpressions();
        ArrayList<Operator> operators = addExpression.getOperators();
        multiplyExpressions.get(0).accept(this);
        for(int i = 1; i<multiplyExpressions.size(); ++i){
            multiplyExpressions.get(i).accept(this);
            operators.get(i-1).accept(this);
        }
    }

    @Override public void visit(MultiplyExpression multiplyExpression) throws Exception {
        ArrayList<Expression> unaryExpressions = multiplyExpression.getExpressions();
        ArrayList<Operator> operators = multiplyExpression.getOperators();
        unaryExpressions.get(0).accept(this);
        for(int i = 1; i<unaryExpressions.size(); ++i){
            unaryExpressions.get(i).accept(this);
            operators.get(i-1).accept(this);
        }
    }

    @Override public void visit(UnaryExpression unaryExpression) throws Exception {
        unaryExpression.getExpression().accept(this);
    }

    @Override public void visit(NegativeUnaryExpression negativeUnaryExpression) throws Exception {
        negativeUnaryExpression.getUnaryExpression().accept(this);
        new NegativeOperator().accept(this);
    }

    @Override public void visit(NotUnaryExpression notUnaryExpression) throws Exception {
        notUnaryExpression.getUnaryExpression().accept(this);
        new NotOperator().accept(this);
    }

    @Override public void visit(Expression expression){
        // This visitor doesn't need to visit this node.
    }

    // Operators
    @Override public void visit(AndOperator andOperator) throws InterpreterException {
        BoolLiteral left = (BoolLiteral) InterpreterUtils.literalCast(environment.popValue(), new BoolVariable(""));
        BoolLiteral right = (BoolLiteral) InterpreterUtils.literalCast(environment.popValue(), new BoolVariable(""));
        boolean lVal = left.getValue().equals("true");
        boolean rVal = right.getValue().equals("true");
        environment.pushValue(new BoolLiteral(rVal && lVal ? "true" : "false"));
    }

    @Override public void visit(DivideOperator divideOperator) throws InterpreterException {
        IntLiteral right = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        IntLiteral left = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        int lVal = Integer.parseInt(left.getValue());
        int rVal = Integer.parseInt(right.getValue());
        environment.pushValue(new IntLiteral(""+lVal/rVal));
    }

    @Override public void visit(EqualOperator equalOperator) throws InterpreterException {
        Literal left = environment.popValue();
        Literal right = environment.popValue();
        environment.pushValue(new BoolLiteral(left.getValue().equals(right.getValue()) ? "true" : "false"));
    }

    @Override public void visit(GreaterEqualOperator greaterEqualOperator) throws InterpreterException {
        IntLiteral right = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        IntLiteral left = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        if(left.getClass().equals(IntLiteral.class) && right.getClass().equals(IntLiteral.class)){
            int lVal = Integer.parseInt(left.getValue());
            int rVal = Integer.parseInt(right.getValue());
            environment.pushValue(new BoolLiteral(lVal >= rVal ? "true" : "false"));
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Can't compare types.");
        }
    }

    @Override public void visit(GreaterOperator greaterOperator) throws InterpreterException {
        IntLiteral right = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        IntLiteral left = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        if(left.getClass().equals(IntLiteral.class) && right.getClass().equals(IntLiteral.class)){
            int lVal = Integer.parseInt(left.getValue());
            int rVal = Integer.parseInt(right.getValue());
            environment.pushValue(new BoolLiteral(lVal > rVal ? "true" : "false"));
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Can't compare types.");
        }
    }

    @Override public void visit(LessEqualOperator lessEqualOperator) throws InterpreterException {
        IntLiteral right = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        IntLiteral left = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        if(left.getClass().equals(IntLiteral.class) && right.getClass().equals(IntLiteral.class)){
            int lVal = Integer.parseInt(left.getValue());
            int rVal = Integer.parseInt(right.getValue());
            environment.pushValue(new BoolLiteral(lVal <= rVal ? "true" : "false"));
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Can't compare types.");
        }
    }

    @Override public void visit(LessOperator lessOperator) throws InterpreterException {
        IntLiteral right = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        IntLiteral left = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        if(left.getClass().equals(IntLiteral.class) && right.getClass().equals(IntLiteral.class)){
            int lVal = Integer.parseInt(left.getValue());
            int rVal = Integer.parseInt(right.getValue());
            environment.pushValue(new BoolLiteral(lVal < rVal ? "true" : "false"));
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Can't compare types.");
        }
    }

    @Override public void visit(MinusOperator minusOperator) throws InterpreterException {
        IntLiteral right = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        IntLiteral left = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        int lVal = Integer.parseInt(left.getValue());
        int rVal = Integer.parseInt(right.getValue());
        environment.pushValue(new IntLiteral(""+(lVal-rVal)));
    }

    @Override public void visit(MultiplyOperator multiplyOperator) throws InterpreterException {
        IntLiteral left = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        IntLiteral right = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        int lVal = Integer.parseInt(left.getValue());
        int rVal = Integer.parseInt(right.getValue());
        environment.pushValue(new IntLiteral(""+lVal*rVal));
    }

    @Override public void visit(NegativeOperator negativeOperator) throws InterpreterException {
        IntLiteral value = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        int val = Integer.parseInt(value.getValue());
        environment.pushValue(new IntLiteral(""+(-val)));
    }

    @Override public void visit(NotEqualOperator notEqualOperator) throws InterpreterException {
        Literal left = environment.popValue();
        Literal right = environment.popValue();
        environment.pushValue(new BoolLiteral((!left.getValue().equals(right.getValue())) ? "true" : "false"));
    }

    @Override public void visit(NotOperator notOperator) throws InterpreterException {
        BoolLiteral value = (BoolLiteral) InterpreterUtils.literalCast(environment.popValue(), new BoolVariable(""));
        boolean val = value.getValue().equals("true");
        environment.pushValue(new BoolLiteral( (!val) ? "true" : "false"));
    }

    @Override public void visit(Operator operator){
        // This visitor doesn't need to visit this node.
    }

    @Override public void visit(OrOperator orOperator) throws InterpreterException {
        BoolLiteral left = (BoolLiteral) InterpreterUtils.literalCast(environment.popValue(), new BoolVariable(""));
        BoolLiteral right = (BoolLiteral) InterpreterUtils.literalCast(environment.popValue(), new BoolVariable(""));
        boolean lVal = left.getValue().equals("true");
        boolean rVal = right.getValue().equals("true");
        environment.pushValue(new BoolLiteral(rVal || lVal ? "true" : "false"));
    }

    @Override public void visit(PlusOperator plusOperator) throws InterpreterException {
        IntLiteral left = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        IntLiteral right = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
        int lVal = Integer.parseInt(left.getValue());
        int rVal = Integer.parseInt(right.getValue());
        environment.pushValue(new IntLiteral(""+(lVal+rVal)));
    }

    // BoardChange
    @Override public void visit(BoardChange boardChange) throws Exception {
        boardChange.getPlayer().accept(this);
        boardChange.getPlayerAction().accept(this);
    }

    @Override public void visit(Activation activation) throws Exception {
        ColorLiteral player = (ColorLiteral) InterpreterUtils.literalCast(environment.popValue(), new ColorVariable(""));
        activation.getWhere().accept(this);
        HexLiteral hex = (HexLiteral) InterpreterUtils.literalCast(environment.popValue(), new HexVariable(""));

        if(activation.getAction().equals("activate")){
            board.activate(Integer.parseInt(hex.getValue().substring(1)), Dictionary.PlayerColors.valueOf(player.getValue()));
        }else if(activation.getAction().equals("deactivate")){
            board.deactivate(Integer.parseInt(hex.getValue().substring(1)), Dictionary.PlayerColors.valueOf(player.getValue()));
        }else{
            throw new InterpreterException(environment.getCurrentStatementLine(), "Wrong activation action.");
        }
    }

    @Override public void visit(PlayerAction playerAction){
        // This visitor doesn't need to visit this node.
    }

    @Override public void visit(UnitsAction unitsAction) throws Exception {
        ColorLiteral player = (ColorLiteral) InterpreterUtils.literalCast(environment.popValue(), new ColorVariable(""));
        Literal from = null;
        Literal to = null;

        if(unitsAction.getFromWhere() != null){
            unitsAction.getFromWhere().accept(this);
            if(environment.peekValue().getClass().equals(HexLiteral.class)){
                from = InterpreterUtils.literalCast(environment.popValue(), new HexVariable(""));
            }else if(environment.peekValue().getClass().equals(PlanetLiteral.class)){
                from = InterpreterUtils.literalCast(environment.popValue(), new PlanetVariable(""));
            }else{
                throw new InterpreterException(environment.getCurrentStatementLine(), "Wrong source in units action.");
            }
        }

        if(unitsAction.getToWhere() != null){
            unitsAction.getToWhere().accept(this);
            if(environment.peekValue().getClass().equals(HexLiteral.class)){
                to = InterpreterUtils.literalCast(environment.popValue(), new HexVariable(""));
            }else if(environment.peekValue().getClass().equals(PlanetLiteral.class)){
                to = InterpreterUtils.literalCast(environment.popValue(), new PlanetVariable(""));
            }else{
                throw new InterpreterException(environment.getCurrentStatementLine(), "Wrong destination in units action.");
            }
        }

        unitsAction.getUnitsList().accept(this);

        for (int i = 0; i < unitsAction.getUnitsList().getUnitAmounts().size(); ++i) {
            IntLiteral intLiteral = (IntLiteral) InterpreterUtils.literalCast(environment.popValue(), new IntVariable(""));
            UnitLiteral unitLiteral = (UnitLiteral) InterpreterUtils.literalCast(environment.popValue(), new UnitVariable(""));
            if(from != null){
                if(from.getClass().equals(HexLiteral.class)){
                    board.changePlayerHexUnitNumber(Integer.parseInt(from.getValue().substring(1)), Dictionary.PlayerColors.valueOf(player.getValue()), Dictionary.SpaceUnits.valueOf(unitLiteral.getValue()), -Integer.parseInt(intLiteral.getValue()));
                }else{
                    board.changePlayerPlanetUnitNumber(Integer.parseInt(from.getValue().substring(1)), Dictionary.PlayerColors.valueOf(player.getValue()), Dictionary.LandUnits.valueOf(unitLiteral.getValue()), -Integer.parseInt(intLiteral.getValue()));
                }
            }
            if(to != null){
                if(to.getClass().equals(HexLiteral.class)){
                    board.changePlayerHexUnitNumber(Integer.parseInt(to.getValue().substring(1)), Dictionary.PlayerColors.valueOf(player.getValue()), Dictionary.SpaceUnits.valueOf(unitLiteral.getValue()), Integer.parseInt(intLiteral.getValue()));
                }else{
                    board.changePlayerPlanetUnitNumber(Integer.parseInt(to.getValue().substring(1)), Dictionary.PlayerColors.valueOf(player.getValue()), Dictionary.LandUnits.valueOf(unitLiteral.getValue()), Integer.parseInt(intLiteral.getValue()));
                }
            }
        }
    }

    @Override public void visit(UnitsList unitsList) throws Exception {
        for (var unitA : unitsList.getUnitAmounts() ) {
            unitA.getUnit().accept(this);
            unitA.getAmount().accept(this);
        }
    }
}
