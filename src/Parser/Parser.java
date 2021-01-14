package Parser;

import Exceptions.ParserException;
import Parser.ProgramTree.*;
import Parser.ProgramTree.BoardChange.*;
import Parser.ProgramTree.ConditionExpresion.*;
import Parser.ProgramTree.ConditionExpresion.Operators.*;
import Parser.ProgramTree.ConditionExpresion.Parenthesis.LeftParenthesis;
import Parser.ProgramTree.ConditionExpresion.Parenthesis.RightParenthesis;
import Parser.ProgramTree.Statements.*;
import Parser.ProgramTree.Value.BoardStateCheck.ActivationCheck;
import Parser.ProgramTree.Value.BoardStateCheck.HexStateCheck;
import Parser.ProgramTree.Value.BoardStateCheck.PlanetStateCheck;
import Parser.ProgramTree.Value.BoardStateCheck.PlayerStateCheck;
import Parser.ProgramTree.Value.FunctionCallValue;
import Parser.ProgramTree.Value.Literals.*;
import Parser.ProgramTree.Value.Value;
import Parser.ProgramTree.Value.VariableValue;
import Parser.ProgramTree.Variables.*;
import Scanner.*;
import Utilities.ParserUtils;

import java.util.ArrayList;
import java.util.Stack;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class implementing parser.
 */
public class Parser {
    private final Scanner scanner;

    public Parser(Scanner scanner) throws Exception {
        this.scanner = scanner;
        scanner.next();
    }

    public Program parse() throws Exception {
        ArrayList<Function> functions = new ArrayList<>();
        Function function;
        while( !compareTokenType(Token.Type.EOF) ){
            function = tryFunctionDefinition();
            functions.add(function);
        }
        return new Program(functions);
    }

    public Function tryFunctionDefinition() throws Exception {
        checkCurrentToken(Token.Type.Type);
        Token type = scanner.get();

        compareTokenType(Token.Type.Identifier);
        Token identifier = scanner.get();

        checkCurrentToken(Token.Type.ParenthesisLeft);
        scanner.next();

        Parameters parameters = tryParameters();

        checkCurrentToken(Token.Type.ParenthesisRight);
        scanner.next();

        Block block = tryBlock();

        return new Function(keywordToVariable(type.getValue(), "return"), identifier.getValue(), parameters, block);
    }

    public Parameters tryParameters() throws Exception {
        Parameters parameters = new Parameters();
        while(compareTokenType(Token.Type.Type)){
            Token type = scanner.get();
            boolean isArray= false;

            if(compareTokenType(Token.Type.BracketsLeft)){
                checkNextToken(Token.Type.BracketsRight);
                scanner.next();
                isArray = true;
            }

            checkCurrentToken(Token.Type.Identifier);
            Token identifier = scanner.get();

            if(isArray){
                parameters.add(keywordToVariable(type.getValue(), identifier.getValue()));
            }else{
                parameters.add(new ArrayVariable(identifier.getValue()));
            }

            if(!compareTokenType(Token.Type.Comma)){
                break;
            }
            scanner.next();
        }
        return parameters;
    }

    public Block tryBlock() throws Exception {
        compareTokenType(Token.Type.BracesLeft);
        scanner.next();

        Block block = new Block();

        Statement statement;
        while((statement = tryStatement()) != null){
            block.add(statement);
        }

        checkCurrentToken(Token.Type.BracesRight);
        scanner.next();
        return block;
    }

    public Statement tryStatement() throws Exception {
        Statement statement;
        if(compareTokenType(Token.Type.BracesLeft)){
            return tryBlock();
        }
        if((statement = tryReturn()) != null){
            return statement;
        }
        if((statement = tryBreak()) != null){
            return statement;
        }
        if((statement = tryContinue()) != null){
            return statement;
        }
        if((statement = tryPrint()) != null){
            return statement;
        }
        if((statement = tryBoardChange()) != null){
            return statement;
        }
        if((statement = tryLoop()) != null){
            return statement;
        }
        if((statement = tryConditional()) != null){
            return statement;
        }
        if((statement = tryFunctionCallOrAssign()) != null){
            return statement;
        }
        if((statement = tryVarOrArrayDeclaration()) != null){
            return statement;
        }
        return null;
    }

    public Break tryBreak() throws Exception {
        if(compareTokenType(Token.Type.Break)){
            checkNextToken(Token.Type.Semicolon);
            scanner.next();
            return new Break();
        }
        return null;
    }

    public Continue tryContinue() throws Exception {
        if(compareTokenType(Token.Type.Continue)){
            checkNextToken(Token.Type.Semicolon);
            scanner.next();
            return new Continue();
        }
        return null;
    }

    public Print tryPrint() throws Exception {
        if(compareTokenType(Token.Type.Print)){
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            Print print = new Print();

            ConditionExpression conditionExpression;
            while((conditionExpression = tryConditionExpression()) != null){
                print.add(conditionExpression);

                if(!compareTokenType(Token.Type.Comma)){
                    break;
                }
                scanner.next();
            }

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();
            checkCurrentToken(Token.Type.Semicolon);
            scanner.next();
            return print;
        }
        return null;
    }

    public Return tryReturn() throws Exception {
        if(compareTokenType(Token.Type.Return)){
            scanner.next();
            ConditionExpression conditionExpression = tryConditionExpression();
            checkCurrentToken(Token.Type.Semicolon);
            scanner.next();
            return new Return(conditionExpression);
        }
        return null;
    }

    public Conditional tryConditional() throws Exception {
        if(compareTokenType(Token.Type.If)){
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            ConditionExpression conditionExpression = tryConditionExpression();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            Block ifBlock = tryBlock();

            if(compareTokenType(Token.Type.Else)){
                scanner.next();
                Block elseBlock = tryBlock();

                return new Conditional(conditionExpression, ifBlock, elseBlock);
            }
            return new Conditional(conditionExpression, ifBlock);
        }
        return null;
    }

    public Loop tryLoop() throws Exception {
        if(compareTokenType(Token.Type.Foreach)){
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            if(compareTokenType(Token.Type.VarType) || compareTokenType(Token.Type.Type)){
                Token typeOrVar = scanner.get();

                checkCurrentToken(Token.Type.Identifier);
                Token variableIdentifier = scanner.get();

                checkCurrentToken(Token.Type.Colon);

                checkNextToken(Token.Type.Identifier);
                Token arrayIdentifier = scanner.get();

                checkCurrentToken(Token.Type.ParenthesisRight);
                scanner.next();

                Block block = tryBlock();
                return new Loop(keywordToVariable(typeOrVar.getValue(), variableIdentifier.getValue()), arrayIdentifier.getValue(), block);
            }
            throw new ParserException(scanner.peek(), Token.Type.Type);
        }
        return null;
    }

    public BoardChange tryBoardChange() throws Exception {
        if(compareTokenType(Token.Type.Player)){
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            Value player = tryValue();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            PlayerAction playerAction = tryPlayerAction();

            BoardChange boardChange = new BoardChange(player, playerAction);

            checkCurrentToken(Token.Type.Semicolon);
            scanner.next();

            return boardChange;
        }
        return null;
    }

    public PlayerAction tryPlayerAction() throws Exception {
        PlayerAction playerAction;
        if((playerAction = tryUnitsAction()) != null){
            return playerAction;
        }
        if((playerAction = tryActivation()) != null){
            return playerAction;
        }
        return null;
    }

    public UnitsAction tryUnitsAction() throws Exception {
        if(compareTokenType(Token.Type.Move) || compareTokenType(Token.Type.Add) || compareTokenType(Token.Type.Remove)){
            Token unitsAction = scanner.get();
            UnitsList unitsList = tryUnitsList();
            Value fromWhere = null;
            if(unitsAction.getType() == Token.Type.Move || unitsAction.getType() == Token.Type.Remove){
                checkCurrentToken(Token.Type.From);
                scanner.next();
                checkCurrentToken(Token.Type.ParenthesisLeft);
                scanner.next();

                fromWhere = tryValue();

                checkCurrentToken(Token.Type.ParenthesisRight);
                scanner.next();
            }
            Value toWhere = null;
            if(unitsAction.getType() == Token.Type.Move||unitsAction.getType() == Token.Type.Add){
                checkCurrentToken(Token.Type.To);
                scanner.next();
                checkCurrentToken(Token.Type.ParenthesisLeft);
                scanner.next();

                toWhere = tryValue();

                checkCurrentToken(Token.Type.ParenthesisRight);
                scanner.next();
            }

            return new UnitsAction(unitsList, fromWhere, toWhere);
        }
        return null;
    }

    public UnitsList tryUnitsList() throws Exception {
        UnitsList unitsList = new UnitsList();
        checkCurrentToken(Token.Type.ParenthesisLeft);
        scanner.next();

        Value unit;
        Value amount;
        while((unit = tryValue()) != null){
            checkCurrentToken(Token.Type.Colon);
            scanner.next();

            amount = tryValue();

            unitsList.add(unit, amount);

            if(!compareTokenType(Token.Type.Comma)){
                break;
            }
            scanner.next();
        }

        checkCurrentToken(Token.Type.ParenthesisRight);
        scanner.next();

        return unitsList;
    }

    public Activation tryActivation() throws Exception {
        if(compareTokenType(Token.Type.Activate) || compareTokenType(Token.Type.Deactivate)){
            Token activation = scanner.get();
            checkCurrentToken(Token.Type.ParenthesisLeft);
            scanner.next();

            Value where = tryValue();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            return new Activation(where, activation.getValue());
        }
        return null;
    }

    public Statement tryFunctionCallOrAssign() throws Exception {
        if(compareTokenType(Token.Type.Identifier)){
            Token identifier = scanner.get();
            if(compareTokenType(Token.Type.ParenthesisLeft)){
                return tryFunctionCallStatement(identifier);
            }
            return tryAssign(identifier);
        }
        return null;
    }

    public FunctionCall tryFunctionCallStatement(Token identifier) throws Exception {
        checkCurrentToken(Token.Type.ParenthesisLeft);
        scanner.next();

        Arguments arguments = tryArguments();

        checkCurrentToken(Token.Type.ParenthesisRight);
        scanner.next();

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new FunctionCall(identifier.getValue(), arguments);
    }

    public Arguments tryArguments() throws Exception {
        Arguments arguments = new Arguments();
        ConditionExpression conditionExpression;
        while((conditionExpression = tryConditionExpression()) != null){
            arguments.add(conditionExpression);

            if(!compareTokenType(Token.Type.Comma)){
                break;
            }
            scanner.next();
        }
        return arguments;
    }

    public Assignment tryAssign(Token identifier) throws Exception {
        String index = null;
        if(compareTokenType(Token.Type.BracketsLeft)){
            checkNextToken(Token.Type.NumberLiteral);
            index = scanner.peek().getValue();
            checkNextToken(Token.Type.BracketsRight);
            scanner.next();
        }
        checkCurrentToken(Token.Type.Equals);
        scanner.next();

        ConditionExpression conditionExpression = tryConditionExpression();

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new Assignment(index, identifier.getValue(), conditionExpression);
    }

    public Statement tryVarOrArrayDeclaration() throws Exception {
        if(compareTokenType(Token.Type.Type) || compareTokenType(Token.Type.VarType)){
            Token type = scanner.get();
            if(compareTokenType(Token.Type.BracketsLeft)){
                return tryArrayDeclaration(type);
            }
            return tryVariableDeclaration(type);
        }
        return null;
    }

    public VariableDeclaration tryVariableDeclaration(Token type) throws Exception {
        checkCurrentToken(Token.Type.Identifier);
        Token identifier = scanner.get();

        checkCurrentToken(Token.Type.Equals);
        scanner.next();

        ConditionExpression conditionExpression = tryConditionExpression();

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new VariableDeclaration(keywordToVariable(type.getValue(), identifier.getValue()), conditionExpression);
    }

    public ArrayDeclaration tryArrayDeclaration(Token type) throws Exception {
        checkCurrentToken(Token.Type.BracketsLeft);
        checkNextToken(Token.Type.BracketsRight);

        checkNextToken(Token.Type.Identifier);
        Token identifier = scanner.get();

        checkCurrentToken(Token.Type.Equals);
        scanner.next();

        checkCurrentToken(Token.Type.Type);
        Token arrayType = scanner.get();

        ArrayDeclaration arrayDeclaration = new ArrayDeclaration(keywordToVariable(arrayType.getValue(), identifier.getValue()));

        checkCurrentToken(Token.Type.BracketsLeft);
        scanner.next();

        if(compareTokenType(Token.Type.NumberLiteral)){
            arrayDeclaration.setSize(Integer.parseInt(scanner.get().getValue()));
        }

        checkCurrentToken(Token.Type.BracketsRight);
        scanner.next();

        if(compareTokenType(Token.Type.BracesLeft)){
            scanner.next();
            Value value;
            while((value = tryValue()) != null){
                arrayDeclaration.add(value);
                if(!compareTokenType(Token.Type.Comma)){
                    break;
                }
                scanner.next();
            }

            checkCurrentToken(Token.Type.BracesRight);
            scanner.next();
        }
        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return arrayDeclaration;
    }

    public Variable keywordToVariable(String type, String name) throws ParserException {
        switch(type){
            case "int":
                return new IntVariable(name);
            case "string":
                return new StringVariable(name);
            case "bool":
                return new BoolVariable(name);
            case "unit":
                return new UnitVariable(name);
            case "color":
                return new ColorVariable(name);
            case "hex":
                return new HexVariable(name);
            case "planet":
                return new PlanetVariable(name);
            case "var":
                return new VarVariable(name);
            default:
                // TODO: co tutaj jak nie wiemy co to?
                break;
        }
        // TODO: co z tym wyjatkiem?
        throw new ParserException(scanner.peek(), "Unknown variable type.");
    }

    public OrCondition tryOrCondition() throws Exception {
        OrCondition orCondition = new OrCondition();

        AndCondition andCondition = tryAndCondition();

        orCondition.add(andCondition);
        while(scanner.peek().getType() == Token.Type.Or){
            scanner.next();
            andCondition = tryAndCondition();

            orCondition.add(andCondition);
        }
        return orCondition;
    }

    public AndCondition tryAndCondition() throws Exception {
        AndCondition andCondition = new AndCondition();

        RelationalCondition relationalCondition = tryRelationalCondition();

        andCondition.add(relationalCondition);
        while(scanner.peek().getType() == Token.Type.And){
            scanner.next();
            relationalCondition = tryRelationalCondition();

            andCondition.add(relationalCondition);
        }
        return andCondition;
    }

    public RelationalCondition tryRelationalCondition() throws Exception {
        RelationalCondition relationalCondition = new RelationalCondition();

        AddExpression addExpression = tryAddExpression();

        relationalCondition.add(addExpression);
        while(isRelation(scanner.peek().getType())){
            scanner.next();
            addExpression = tryAddExpression();

            relationalCondition.add(addExpression);
        }
        return relationalCondition;
    }

    public boolean isRelation(Token.Type type){
        return type == Token.Type.Greater || type == Token.Type.Less || type == Token.Type.LessEqual || type == Token.Type.GreaterEqual || type == Token.Type.Equal || type == Token.Type.NotEqual;
    }

    public AddExpression tryAddExpression() throws Exception {
        AddExpression addExpression = new AddExpression();

        MultiplyExpression multiplyExpression = tryMultiplyExpression();

        addExpression.add(multiplyExpression);

        while(isAdd(scanner.peek().getType())){
            scanner.next();

            multiplyExpression = tryMultiplyExpression();

            addExpression.add(multiplyExpression);
        }
        return addExpression;
    }

    public boolean isAdd(Token.Type type){
        return type == Token.Type.Add || type == Token.Type.Minus;
    }

    public MultiplyExpression tryMultiplyExpression() throws Exception {
        MultiplyExpression multiplyExpression = new MultiplyExpression();

        UnaryExpression expression = tryUnaryExpression();

        multiplyExpression.add(expression);
        while(isMultiply(scanner.peek().getType())){
            scanner.next();

            expression = tryUnaryExpression();

            multiplyExpression.add(expression);
        }
        return multiplyExpression;
    }

    public boolean isMultiply(Token.Type type){
        return type == Token.Type.Multiply || type == Token.Type.Divide;
    }

    public UnaryExpression tryUnaryExpression() throws Exception {
        UnaryExpression unaryExpression;
        if((unaryExpression = tryNegativeUnaryExpression()) != null){
            return unaryExpression;
        }
        if((unaryExpression = tryNotUnaryExpression()) != null){
            return unaryExpression;
        }
        return tryExpression();
    }

    public NegativeUnaryExpression tryNegativeUnaryExpression() throws Exception {
        if(compareTokenType(Token.Type.Minus)){
            scanner.next();
            return (NegativeUnaryExpression) tryUnaryExpression();
        }
        return null;
    }

    public NotUnaryExpression tryNotUnaryExpression() throws Exception {
        if(compareTokenType(Token.Type.Not)){
            scanner.next();
            return (NotUnaryExpression) tryUnaryExpression();
        }
        return null;
    }

    public Expression tryExpression() throws Exception {
        if(compareTokenType(Token.Type.ParenthesisLeft)){
            scanner.next();

            OrCondition orCondition = tryOrCondition();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            return orCondition;
        }else{
            return tryValue();
        }
    }

    public ConditionExpression tryConditionExpression() throws Exception {
        // Shunting-yard algorithm
        if(scanner.peek().getType() == Token.Type.ParenthesisRight){
            return null;
        }

        Stack<Node> operandStack = new Stack<>();
        Stack<Node> operatorStack = new Stack<>();
        Token previousToken = null;

        while(isForValueOrCondition(scanner.peek().getType())){
            Token token = scanner.peek();
            Value value = tryValue();
            if(value != null && (previousToken == null || !isForValue(previousToken.getType()))){
                operandStack.push(new ValueNode(value));
            }else if(token.getType() == Token.Type.ParenthesisLeft){
                operatorStack.push(new LeftParenthesis());
            }else if(token.getType() == Token.Type.ParenthesisRight){
                if(operatorStack.isEmpty() && !operandStack.isEmpty()){
                    break;
                }
                while(!operatorStack.isEmpty() && operatorStack.peek().getClass() != LeftParenthesis.class){
                    prepareNode(operandStack, operatorStack);
                }
                if(operatorStack.isEmpty() && operandStack.size() == 1){
                    break;
                }else if(operatorStack.isEmpty()){
                    throw new ParserException(token, "Wrong number of opening parenthesis.");
                }
                operatorStack.pop();
            }else if(ParserUtils.forConditionExpression.get(token.getType()) != null){
                Node operator;

                if(previousToken == null || previousToken.getType() == Token.Type.ParenthesisLeft || ParserUtils.forConditionExpression.get(previousToken.getType()) != null){
                    if(token.getType() == Token.Type.Minus){
                        operator = new NegativeOperator();
                    }else if(token.getType() == Token.Type.Not){
                        operator = new NotOperator();
                    }else{
                        throw new ParserException(token, "Wrong expression.");
                    }
                }else{
                    operator = tokenTypeToOperator(token.getType());
                }

                while( !operatorStack.isEmpty() && !operandStack.isEmpty() && operatorStack.peek().getClass() != LeftParenthesis.class && operatorStack.peek().getClass() != RightParenthesis.class && ParserUtils.compareOperators(operatorStack.peek().getClass(), operator.getClass() ) && operator.getClass() != NegativeOperator.class){
                    prepareNode(operandStack, operatorStack);
                }
                operatorStack.push(operator);
            }else{
                throw new ParserException(token, "Wrong expression.");
            }
            previousToken = token;
            if(value == null){
                scanner.next();
            }
        }
        while(!operatorStack.isEmpty()){
            prepareNode(operandStack, operatorStack);
        }
        if(operandStack.size() != 1){
            throw new ParserException(scanner.peek(), "Wrong expression.");
        }
        return new ConditionExpression(operandStack.pop());
    }

    public Operator tokenTypeToOperator(Token.Type type) throws ParserException {
        switch(type){
            case Or:
                return new OrOperator();
            case And:
                return new AndOperator();
            case Greater:
                return new GreaterOperator();
            case GreaterEqual:
                return new GreaterEqualOperator();
            case Less:
                return new LessOperator();
            case LessEqual:
                return new LessEqualOperator();
            case Equal:
                return new EqualOperator();
            case NotEqual:
                return new NotEqualOperator();
            case Not:
                return new NotOperator();
            case Plus:
                return new PlusOperator();
            case Minus:
                return new MinusOperator();
            case Multiply:
                return new MultiplyOperator();
            case Divide:
                return new DivideOperator();
            default:
                break;
        }
        // TODO: co z tym wyjatkiem?
        throw new ParserException(scanner.peek(), "Unknown operator type.");
    }

    private void prepareNode(Stack<Node> operandStack, Stack<Node> operatorStack) throws ParserException {
        Node parent = operatorStack.pop();
        if(parent.getClass() == LeftParenthesis.class || parent.getClass() == RightParenthesis.class){
            throw new ParserException(scanner.peek(), "Wrong parenthesis number.");
        }
        Node rightChild = operandStack.pop();
        Node leftChild = null;

        if(ParserUtils.operatorOperandsNumber.get(parent.getClass())>1){
            leftChild = operandStack.pop();
        }
        parent.left = leftChild;
        parent.right = rightChild;
        operandStack.push(parent);
    }

    private boolean isForValueOrCondition(Token.Type type){
        return isForValue(type) || (ParserUtils.forConditionExpression.get(type) != null) || type == Token.Type.ParenthesisLeft || type == Token.Type.ParenthesisRight;
    }

    private boolean isForValue(Token.Type type){
        return type == Token.Type.Identifier || isLiteral(type) || isBoardCheck(type);
    }

    private boolean isLiteral(Token.Type type){
        return type == Token.Type.NumberLiteral || type == Token.Type.StringLiteral || type == Token.Type.BoolLiteral || type == Token.Type.UnitLiteral ||
                type == Token.Type.ColorLiteral || type == Token.Type.HexLiteral || type == Token.Type.PlanetLiteral;
    }

    private boolean isBoardCheck(Token.Type type){
        return type == Token.Type.Player || type == Token.Type.Type;
    }

    public Value tryValue() throws Exception {
        Token token = scanner.peek();
        if(isLiteral(scanner.peek().getType())){
            scanner.next();
            return tokenTypeToLiteral(token.getType(), token.getValue());
        }
        if (token.getType() == Token.Type.Identifier){
            Token identifier = scanner.get();
            if(compareTokenType(Token.Type.ParenthesisLeft)){
                return tryFunctionCallValue(identifier);
            }
            if(compareTokenType(Token.Type.BracketsLeft)){
                checkNextToken(Token.Type.NumberLiteral);
                Token number = scanner.get();
                checkCurrentToken(Token.Type.BracketsRight);
                scanner.next();

                return new VariableValue(identifier.getValue(), Integer.parseInt(number.getValue()));
            }
            return new VariableValue(identifier.getValue());
        }
        if(isBoardCheck(token.getType())){
            return tryBoardStateCheck();
        }
        return null;
    }

    public Literal tokenTypeToLiteral(Token.Type type, String value) throws ParserException {
        switch(type){
            case NumberLiteral:
                return new IntLiteral(value);
            case StringLiteral:
                return new StringLiteral(value);
            case BoolLiteral:
                return new BoolLiteral(value);
            case UnitLiteral:
                return new UnitLiteral(value);
            case ColorLiteral:
                return new ColorLiteral(value);
            case HexLiteral:
                return new HexLiteral(value);
            case PlanetLiteral:
                return new PlanetLiteral(value);
            default:
                // TODO: co tutaj?
                break;
        }
        // TODO: co z tym wyjatkiem?
        throw new ParserException(scanner.peek(), "Unknown literal type.");
    }

    public FunctionCallValue tryFunctionCallValue(Token identifier) throws Exception {
        checkCurrentToken(Token.Type.ParenthesisLeft);
        scanner.next();

        Arguments arguments = tryArguments();

        checkCurrentToken(Token.Type.ParenthesisRight);
        scanner.next();

        return new FunctionCallValue(identifier.getValue(), arguments);
    }

    public Value tryBoardStateCheck() throws Exception {
        if(scanner.peek().getType() == Token.Type.Player){
            return tryPlayerStateCheckOrActivationCheck();
        }else{
            return tryPlanetOrHexStateCheck();
        }
    }

    public Value tryPlanetOrHexStateCheck() throws Exception {
        if(compareTokenType(Token.Type.Type) &&(scanner.peek().getValue().equals("hex") || scanner.peek().getValue().equals("planet"))){
            Token type = scanner.peek();
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();
            Value place = tryValue();
            checkCurrentToken(Token.Type.ParenthesisRight);
            checkNextToken(Token.Type.Has);
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();
            Value unit = tryValue();
            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();
            if(type.getValue().equals("planet")){
                return new PlanetStateCheck(place, unit);
            }else if(type.getValue().equals("hex")){
                return new HexStateCheck(place, unit);
            }
        }
        return null;
    }

    public Value tryPlayerStateCheckOrActivationCheck() throws Exception {
        if(compareTokenType(Token.Type.Player)){
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            Value player = tryValue();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            if(compareTokenType(Token.Type.Has)){
                checkNextToken(Token.Type.ParenthesisLeft);
                scanner.next();
                Value unit = tryValue();
                checkCurrentToken(Token.Type.ParenthesisRight);
                checkNextToken(Token.Type.At);
                checkNextToken(Token.Type.ParenthesisLeft);
                scanner.next();
                Value place = tryValue();
                checkCurrentToken(Token.Type.ParenthesisRight);
                scanner.next();
                return new PlayerStateCheck(player, unit, place);
            }else if(compareTokenType(Token.Type.Activated)){
                checkNextToken(Token.Type.ParenthesisLeft);
                scanner.next();
                Value hex = tryValue();
                checkCurrentToken(Token.Type.ParenthesisRight);
                scanner.next();
                return new ActivationCheck(player, hex);
            }else{
                throw new ParserException(scanner.peek(), "Expected \"has\" or \"activated\"");
            }
        }
        return null;
    }

    private void checkNextToken(Token.Type type) throws Exception {
        scanner.next();
        checkCurrentToken(type);
    }

    private void checkCurrentToken(Token.Type type) throws Exception {
        Token token = scanner.peek();
        if(!compareTokenType(type)){
            throw new ParserException(token, type);
        }
    }

    private boolean compareTokenType(Token.Type type){
        return scanner.peek().getType() == type;
    }
}