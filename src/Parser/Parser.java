package Parser;

import Exceptions.ParserException;
import Parser.ProgramTree.*;
import Parser.ProgramTree.BoardChange.*;
import Parser.ProgramTree.ConditionExpresion.ConditionExpression;
import Parser.ProgramTree.ConditionExpresion.Node;
import Parser.ProgramTree.Statements.*;
import Parser.ProgramTree.Value.BoardStateCheck.ActivationCheck;
import Parser.ProgramTree.Value.BoardStateCheck.HexStateCheck;
import Parser.ProgramTree.Value.BoardStateCheck.PlanetStateCheck;
import Parser.ProgramTree.Value.BoardStateCheck.PlayerStateCheck;
import Parser.ProgramTree.Value.FunctionCallValue;
import Parser.ProgramTree.Value.Literal;
import Parser.ProgramTree.Value.Value;
import Parser.ProgramTree.Value.VariableValue;
import Scanner.*;
import Utilities.ParserUtils;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    private final Scanner scanner;

    public Parser(Scanner scanner){
        this.scanner = scanner;
    }

    public Program parse() throws Exception {
        ArrayList<Function> functions = new ArrayList<>();
        Function function;
        scanner.next();
        while( !compareTokenType(Token.Type.EOF) ){
            function = tryFunctionDefinition();
            functions.add(function);
        }
        return new Program(functions);
    }

    private Function tryFunctionDefinition() throws Exception {
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

        return new Function(new Variable(ParserUtils.keywordToVariableType.get(type.getValue())), identifier.getValue(), parameters, block);
    }

    private Parameters tryParameters() throws Exception {
        Parameters parameters = new Parameters();
        while(compareTokenType(Token.Type.Type)){
            Token type = scanner.get();

            checkCurrentToken(Token.Type.Identifier);
            Token identifier = scanner.get();

            parameters.add(new Variable(ParserUtils.keywordToVariableType.get(type.getValue()), identifier.getValue()));

            if(!compareTokenType(Token.Type.Comma)){
                break;
            }
            scanner.next();
        }
        return parameters;
    }

    private Block tryBlock() throws Exception {
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

    private Statement tryStatement() throws Exception {
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

    private Break tryBreak() throws Exception {
        if(compareTokenType(Token.Type.Break)){
            checkNextToken(Token.Type.Semicolon);
            scanner.next();
            return new Break();
        }
        return null;
    }

    private Continue tryContinue() throws Exception {
        if(compareTokenType(Token.Type.Break)){
            checkNextToken(Token.Type.Semicolon);
            scanner.next();
            return new Continue();
        }
        return null;
    }

    private Print tryPrint() throws Exception {
        if(compareTokenType(Token.Type.Print)){
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            Print print = new Print();

            ConditionExpression conditionExpression;
            while((conditionExpression = tryConditionExpression()) != null){
                print.add(conditionExpression);
            }

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();
            return print;
        }
        return null;
    }

    private Return tryReturn() throws Exception {
        if(compareTokenType(Token.Type.Return)){
            scanner.next();
            ConditionExpression conditionExpression = tryConditionExpression();
            checkCurrentToken(Token.Type.Semicolon);
            scanner.next();
            return new Return(conditionExpression);
        }
        return null;
    }

    private Conditional tryConditional() throws Exception {
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

    private Loop tryLoop() throws Exception {
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
                return new Loop(new Variable(ParserUtils.keywordToVariableType.get(typeOrVar.getValue()), variableIdentifier.getValue()), arrayIdentifier.getValue(), block);
            }
            throw new ParserException(scanner.peek(), Token.Type.Type);
        }
        return null;
    }

    private BoardChange tryBoardChange() throws Exception {
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

    private PlayerAction tryPlayerAction() throws Exception {
        PlayerAction playerAction;
        if((playerAction = tryUnitsAction()) != null){
            return playerAction;
        }
        if((playerAction = tryActivation()) != null){
            return playerAction;
        }
        return null;
    }

    private UnitsAction tryUnitsAction() throws Exception {
        if(compareTokenType(Token.Type.UnitsAction)){
            switch (scanner.peek().getValue()) {
                case "move":
                case "add":
                case "remove":
                    break;
                default: throw new Exception(scanner.peek().getPosition().toString() + " Invalid action.");
            }
            Token unitsAction = scanner.get();
            UnitsList unitsList = tryUnitsList();
            Value fromWhere = null;
            if(unitsAction.getValue().equals("move") || unitsAction.getValue().equals("remove")){
                checkCurrentToken(Token.Type.From);
                scanner.next();
                checkCurrentToken(Token.Type.ParenthesisLeft);
                scanner.next();

                fromWhere = tryValue();

                checkCurrentToken(Token.Type.ParenthesisRight);
                scanner.next();
            }
            Value toWhere = null;
            if(unitsAction.getValue().equals("move")||unitsAction.getValue().equals("remove")){
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

    private UnitsList tryUnitsList() throws Exception {
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

    private Activation tryActivation() throws Exception {
        if(compareTokenType(Token.Type.Activation)){
            switch (scanner.peek().getValue()) {
                case "activate":
                case "deactivate":
                    break;
                default: throw new Exception(scanner.peek().getPosition().toString() + " Invalid action.");
            }
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

    private Statement tryFunctionCallOrAssign() throws Exception {
        if(compareTokenType(Token.Type.Identifier)){
            Token identifier = scanner.get();
            if(compareTokenType(Token.Type.ParenthesisLeft)){
                return tryFunctionCallStatement(identifier);
            }
            return tryAssign(identifier);
        }
        return null;
    }

    private FunctionCall tryFunctionCallStatement(Token identifier) throws Exception {
        checkCurrentToken(Token.Type.ParenthesisLeft);
        scanner.next();

        Arguments arguments = tryArguments();

        checkCurrentToken(Token.Type.ParenthesisRight);
        scanner.next();

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new FunctionCall(identifier.getValue(), arguments);
    }

    private Arguments tryArguments() throws Exception {
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

    private Assignment tryAssign(Token identifier) throws Exception {
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

    private Statement tryVarOrArrayDeclaration() throws Exception {
        if(compareTokenType(Token.Type.Type) || compareTokenType(Token.Type.VarType)){
            Token type = scanner.get();
            if(compareTokenType(Token.Type.BracketsLeft)){
                return tryArrayDeclaration(type);
            }
            return tryVariableDeclaration(type);
        }
        return null;
    }

    private VariableDeclaration tryVariableDeclaration(Token type) throws Exception {
        checkCurrentToken(Token.Type.Identifier);
        Token identifier = scanner.get();

        checkCurrentToken(Token.Type.Equals);
        scanner.next();

        ConditionExpression conditionExpression = tryConditionExpression();

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new VariableDeclaration(new Variable(ParserUtils.keywordToVariableType.get(type.getValue()), identifier.getValue()), conditionExpression);
    }

    private ArrayDeclaration tryArrayDeclaration(Token type) throws Exception {
        checkCurrentToken(Token.Type.BracketsLeft);
        checkNextToken(Token.Type.BracketsRight);

        checkNextToken(Token.Type.Identifier);
        Token identifier = scanner.get();

        checkCurrentToken(Token.Type.Equals);
        scanner.next();

        checkCurrentToken(Token.Type.Type);
        Token arrayType = scanner.get();

        ArrayDeclaration arrayDeclaration = new ArrayDeclaration(ParserUtils.keywordToVariableType.get(arrayType.getValue()), identifier.getValue());

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
            }

            checkCurrentToken(Token.Type.BracesRight);
            scanner.next();
        }
        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return arrayDeclaration;
    }

    private ConditionExpression tryConditionExpression() throws Exception {
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
                operandStack.push(new Node(value));
            }else if(token.getType() == Token.Type.ParenthesisLeft){
                operatorStack.push(new Node(Node.Parenthesis.Left));
            }else if(token.getType() == Token.Type.ParenthesisRight){
                if(operatorStack.isEmpty() && !operandStack.isEmpty()){
                    break;
                }
                while(!operatorStack.isEmpty() && operatorStack.peek().parenthesis != Node.Parenthesis.Left){
                    Node parent = operatorStack.pop();
                    Node rightChild = operandStack.pop();
                    Node leftChild = null;

                    if(ParserUtils.operatorOperandsNumber.get(parent.operator)>1){
                        leftChild = operandStack.pop();
                    }
                    parent.left = leftChild;
                    parent.right = rightChild;
                    operandStack.push(parent);
                }
                if(operatorStack.isEmpty()){
                    throw new ParserException(token, "Wrong number of opening parenthesis.");
                }
                operatorStack.pop();
            }else if(ParserUtils.forConditionExpression.get(token.getType()) != null){
                Node operator;

                if(previousToken == null || previousToken.getType() == Token.Type.ParenthesisLeft || ParserUtils.forConditionExpression.get(previousToken.getType()) != null){
                    if(token.getType() == Token.Type.Minus){
                        operator = new Node(Node.Operator.Negative);
                    }else if(token.getType() == Token.Type.Not){
                        operator = new Node(Node.Operator.Not);
                    }else{
                        throw new ParserException(token, "Wrong expression.");
                    }
                }else{
                    operator = new Node(ParserUtils.forConditionExpression.get(token.getType()));
                }

                while( !operatorStack.isEmpty() && operatorStack.peek().parenthesis == Node.Parenthesis.Non && ParserUtils.compareOperators(operatorStack.peek().operator, operator.operator )){
                    Node parent = operatorStack.pop();
                    Node rightChild = operandStack.pop();
                    Node leftChild = null;

                    if(ParserUtils.operatorOperandsNumber.get(parent.operator)>1){
                        leftChild = operandStack.pop();
                    }
                    parent.left = leftChild;
                    parent.right = rightChild;
                    operandStack.push(parent);
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
            Node parent = operatorStack.pop();
            Node rightChild = operandStack.pop();
            Node leftChild = null;

            if(ParserUtils.operatorOperandsNumber.get(parent.operator)>1){
                leftChild = operandStack.pop();
            }
            parent.left = leftChild;
            parent.right = rightChild;
            operandStack.push(parent);
        }
        if(operandStack.size() != 1){
            throw new ParserException(scanner.peek(), "Wrong expression.");
        }
        return new ConditionExpression(operandStack.pop());
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
        return type == Token.Type.Player || type == Token.Type.Planet || type == Token.Type.Hex;
    }

    private Value tryValue() throws Exception {
        Token token = scanner.peek();
        if(isLiteral(scanner.peek().getType())){
            scanner.next();
            return new Literal(ParserUtils.tokenTypeToLiteralType.get(token.getType()), token.getValue());
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

    private FunctionCallValue tryFunctionCallValue(Token identifier) throws Exception {
        checkCurrentToken(Token.Type.ParenthesisLeft);
        scanner.next();

        Arguments arguments = tryArguments();

        checkCurrentToken(Token.Type.ParenthesisRight);
        scanner.next();

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new FunctionCallValue(identifier.getValue(), arguments);
    }

    private Value tryBoardStateCheck() throws Exception {
        if(scanner.peek().getType() == Token.Type.Player){
            return tryPlayerStateCheckOrActivationCheck();
        }else{
            return tryPlanetOrHexStateCheck();
        }
    }

    private Value tryPlanetOrHexStateCheck() throws Exception {
        if(compareTokenType(Token.Type.Planet)||compareTokenType(Token.Type.Hex)){
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
            if(type.getType() == Token.Type.Planet){
                return new PlanetStateCheck(place, unit);
            }else if(type.getType() == Token.Type.Hex){
                return new HexStateCheck(place, unit);
            }
        }
        return null;
    }

    private Value tryPlayerStateCheckOrActivationCheck() throws Exception {
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
