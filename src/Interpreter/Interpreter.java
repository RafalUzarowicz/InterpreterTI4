package Interpreter;

import Board.Board;
import Exceptions.InterpreterException;
import Interpreter.Environment.BlockContext;
import Interpreter.Environment.CallContext;
import Interpreter.Environment.Environment;
import Utilities.Dictionary;
import Utilities.InterpreterUtils;
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
import Utilities.ScannerUtils;

import java.util.Map;

public class Interpreter implements IVisitor{
    private final Board board;
    private final Program program;
    private final Environment environment;

    public Interpreter(Program program, Board board){
        this.program = program;
        this.board = board;
        this.environment = new Environment();
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
    }

    @Override public void visit(Function function) throws Exception {
        environment.pushCallContext();
        CallContext callContext = environment.peekCallContext();
        callContext.pushBlockContext();
        function.getParameters().accept(this);
        if(function.getArguments() != null){
            function.getArguments().accept(this);
        }
        function.getBlock().accept(this);
        callContext.popBlockContext();
        environment.popCallContext();
    }

    @Override public void visit(Block block) throws Exception {
        environment.peekCallContext().pushBlockContext();
        for (var stm: block.getStatements()) {
            if(environment.hasReturned() || environment.hasBroken() || environment.hasContinued()){
                break;
            }
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
                throw new InterpreterException("Parameter "+param.getName()+" already defined.");
            }
        }
    }

    @Override public void visit(Arguments arguments) throws Exception {
        BlockContext blockContext = environment.peekCallContext().peekBlockContext();
        if(blockContext.getSize() != arguments.getSize()){
            throw new InterpreterException("Wrong number of arguments.");
        }
        int i = 0;
        Assignment assignment;
        for (Map.Entry<String, Variable> entry : blockContext.getMap().entrySet()) {
            String name = entry.getKey();
            assignment = new Assignment(null, name, arguments.getArguments().get(i));
            assignment.accept(this);
            ++i;
        }
    }

    // Variables
    @Override public void visit(ArrayVariable arrayVariable){
        // TODO: implementacja
    }
    @Override public void visit(BoolVariable boolVariable){
        // TODO: implementacja
    }
    @Override public void visit(ColorVariable colorVariable){
        // TODO: implementacja
    }
    @Override public void visit(HexVariable hexVariable){
        // TODO: implementacja
    }
    @Override public void visit(IntVariable intVariable){
        // TODO: implementacja
    }
    @Override public void visit(PlanetVariable planetVariable){
        // TODO: implementacja
    }
    @Override public void visit(StringVariable stringVariable){
        // TODO: implementacja
    }
    @Override public void visit(UnitVariable unitVariable){
        // TODO: implementacja
    }
    @Override public void visit(Variable variable){
        // TODO: implementacja
    }
    @Override public void visit(VarVariable varVariable){
        // TODO: implementacja
    }

    // Value
    @Override public void visit(VariableValue variableValue){
        // TODO: implementacja
    }
    @Override public void visit(Value value){
        // TODO: implementacja
    }
    @Override public void visit(FunctionCallValue functionCallValue){
        // TODO: implementacja
    }

    // Literals
    @Override public void visit(BoolLiteral boolLiteral){
        // TODO: implementacja
    }
    @Override public void visit(ColorLiteral colorLiteral){
        // TODO: implementacja
    }
    @Override public void visit(HexLiteral hexLiteral){
        // TODO: implementacja
    }
    @Override public void visit(IntLiteral intLiteral){
        // TODO: implementacja
    }
    @Override public void visit(Literal literal){
        // TODO: implementacja
    }
    @Override public void visit(PlanetLiteral planetLiteral){
        // TODO: implementacja
    }
    @Override public void visit(StringLiteral stringLiteral){
        // TODO: implementacja
    }
    @Override public void visit(UnitLiteral unitLiteral){
        // TODO: implementacja
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
            throw new InterpreterException("Hex or planet value expected.");
        }
        playerStateCheck.getUnit().accept(this);
        UnitLiteral unit = (UnitLiteral) InterpreterUtils.literalCast(environment.popValue(), new UnitVariable(""));

        int count;
        if(place.getClass().equals(HexLiteral.class)){
            count = board.getPlayerHexUnitNumber(Integer.parseInt(place.getValue().substring(1)),Dictionary.PlayerColors.valueOf(player.getValue()), Dictionary.SpaceUnits.valueOf(unit.getValue()));
        }else if(place.getClass().equals(PlanetLiteral.class)){
            count = board.getPlayerPlanetUnitNumber(Integer.parseInt(place.getValue().substring(1)),Dictionary.PlayerColors.valueOf(player.getValue()), Dictionary.LandUnits.valueOf(unit.getValue()));
        }else{
            throw new InterpreterException("Hex or planet value expected.");
        }

        environment.pushValue(new IntLiteral(Integer.toString(count)));
    }

    // Statements
    @Override public void visit(ArrayDeclaration arrayDeclaration){
        // TODO: implementacja
        ArrayVariable arrayVariable = arrayDeclaration.getType();
        Variable variable = arrayVariable.getVariable();




    }

    @Override public void visit(Assignment assignment) throws Exception {
        CallContext callContext = environment.peekCallContext();

        Variable variable = callContext.getVariable(assignment.getIdentifier());

        OrCondition orCondition = assignment.getValue();

        orCondition.accept(this);

        Literal literal = environment.popValue();

        Literal casted = InterpreterUtils.literalCast(literal, variable);

        if(ArrayVariable.class.equals(variable.getClass())){
            int index = Integer.parseInt(assignment.getIndex());
            ((ArrayVariable) variable).setValue(index, casted.getValue());
        }else{
            variable.setValue(casted.getValue());
        }
    }

    @Override public void visit(Break breakStm){
        environment.setHasBroken(true);
    }

    @Override public void visit(Conditional conditional){
        // TODO: implementacja
    }

    @Override public void visit(Continue continueStm){
        environment.setHasContinued(true);
    }

    @Override public void visit(FunctionCall functionCallStm){
        // TODO: implementacja
    }
    @Override public void visit(Loop loop){
        // TODO: implementacja
    }

    @Override public void visit(Print print) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        Literal literal;
        for( var orCondition: print.getConditionExpressions()){

            orCondition.accept(this);

            literal = environment.popValue();

            stringBuilder.append(literal.getValue());
        }
        System.out.println(stringBuilder.toString());
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
    @Override public void visit(OrCondition orCondition){
        // TODO: implementacja
    }
    @Override public void visit(AddExpression addExpression){
        // TODO: implementacja
    }
    @Override public void visit(AndCondition andCondition){
        // TODO: implementacja
    }
    @Override public void visit(Expression expression){
        // TODO: implementacja
    }
    @Override public void visit(MultiplyExpression multiplyExpression){
        // TODO: implementacja
    }
    @Override public void visit(NegativeUnaryExpression negativeUnaryExpression){
        // TODO: implementacja
    }
    @Override public void visit(NotUnaryExpression notUnaryExpression){
        // TODO: implementacja
    }
    @Override public void visit(RelationalCondition relationalCondition){
        // TODO: implementacja
    }
    @Override public void visit(UnaryExpression unaryExpression){
        // TODO: implementacja
    }

    // Operators
    @Override public void visit(AndOperator andOperator){
        // TODO: implementacja
    }
    @Override public void visit(DivideOperator divideOperator){
        // TODO: implementacja
    }
    @Override public void visit(EqualOperator equalOperator){
        // TODO: implementacja
    }
    @Override public void visit(GreaterEqualOperator greaterEqualOperator){
        // TODO: implementacja
    }
    @Override public void visit(GreaterOperator greaterOperator){
        // TODO: implementacja
    }
    @Override public void visit(LessEqualOperator lessEqualOperator){
        // TODO: implementacja
    }
    @Override public void visit(LessOperator lessOperator){
        // TODO: implementacja
    }
    @Override public void visit(MinusOperator minusOperator){
        // TODO: implementacja
    }
    @Override public void visit(MultiplyOperator multiplyOperator){
        // TODO: implementacja
    }
    @Override public void visit(NegativeOperator negativeOperator){
        // TODO: implementacja
    }
    @Override public void visit(NotEqualOperator notEqualOperator){
        // TODO: implementacja
    }
    @Override public void visit(NotOperator notOperator){
        // TODO: implementacja
    }
    @Override public void visit(Operator operator){
        // TODO: implementacja
    }
    @Override public void visit(OrOperator orOperator){
        // TODO: implementacja
    }
    @Override public void visit(PlusOperator plusOperator){
        // TODO: implementacja
    }

    // BoardChange
    @Override public void visit(Activation activation){
        // TODO: implementacja
    }
    @Override public void visit(BoardChange boardChange){
        // TODO: implementacja
    }
    @Override public void visit(PlayerAction playerAction){
        // TODO: implementacja
    }
    @Override public void visit(UnitsAction unitsAction){
        // TODO: implementacja
    }
    @Override public void visit(UnitsList unitsList){
        // TODO: implementacja
    }
}
