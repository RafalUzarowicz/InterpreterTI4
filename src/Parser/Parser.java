package Parser;

import Exceptions.ParserException;
import Scanner.Scanner;
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
import Utilities.Token;

import java.util.HashMap;

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
        HashMap<String, Function> functions = new HashMap<>();
        Function function;
        while (!compareTokenType(Token.Type.EOF)) {
            function = tryFunctionDefinition();
            functions.put(function.getIdentifier(), function);
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

        return new Function(keywordToVariable(type.getValue(), "return"), identifier.getValue(), parameters, block, identifier.getPosition());
    }

    public Parameters tryParameters() throws Exception {
        Parameters parameters = new Parameters();
        while (compareTokenType(Token.Type.Type)) {
            Token type = scanner.get();

            checkCurrentToken(Token.Type.Identifier);
            Token identifier = scanner.get();

            parameters.add(keywordToVariable(type.getValue(), identifier.getValue()));

            if (!compareTokenType(Token.Type.Comma)) {
                break;
            }
            scanner.next();
        }
        return parameters;
    }

    public Block tryBlock() throws Exception {
        compareTokenType(Token.Type.BracesLeft);
        Token token = scanner.get();

        Block block = new Block(token.getPosition());

        Statement statement;
        while ((statement = tryStatement()) != null) {
            block.add(statement);
        }

        checkCurrentToken(Token.Type.BracesRight);
        scanner.next();
        return block;
    }

    public Statement tryStatement() throws Exception {
        Statement statement;
        if (compareTokenType(Token.Type.BracesLeft)) {
            return tryBlock();
        }
        if ((statement = tryReturn()) != null) {
            return statement;
        }
        if ((statement = tryBreak()) != null) {
            return statement;
        }
        if ((statement = tryContinue()) != null) {
            return statement;
        }
        if ((statement = tryPrint()) != null) {
            return statement;
        }
        if ((statement = tryBoardChange()) != null) {
            return statement;
        }
        if ((statement = tryLoop()) != null) {
            return statement;
        }
        if ((statement = tryConditional()) != null) {
            return statement;
        }
        if ((statement = tryFunctionCallOrAssign()) != null) {
            return statement;
        }
        if ((statement = tryVarOrArrayDeclaration()) != null) {
            return statement;
        }
        return null;
    }

    public Break tryBreak() throws Exception {
        if (compareTokenType(Token.Type.Break)) {
            Token token = scanner.peek();
            checkNextToken(Token.Type.Semicolon);
            scanner.next();
            return new Break(token.getPosition());
        }
        return null;
    }

    public Continue tryContinue() throws Exception {
        if (compareTokenType(Token.Type.Continue)) {
            Token token = scanner.peek();
            checkNextToken(Token.Type.Semicolon);
            scanner.next();
            return new Continue(token.getPosition());
        }
        return null;
    }

    public Print tryPrint() throws Exception {
        if (compareTokenType(Token.Type.Print)) {
            Token token = scanner.peek();
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            Print print = new Print(token.getPosition());

            Expression orCondition;
            while ((orCondition = tryOrCondition()) != null) {
                print.add(orCondition);

                if (!compareTokenType(Token.Type.Comma)) {
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
        if (compareTokenType(Token.Type.Return)) {
            Token token = scanner.get();
            Expression orCondition = tryOrCondition();
            if (orCondition == null) {
                throw new ParserException(scanner.peek(), "Missing value to return.");
            }
            checkCurrentToken(Token.Type.Semicolon);
            scanner.next();
            return new Return(orCondition, token.getPosition());
        }
        return null;
    }

    public Conditional tryConditional() throws Exception {
        if (compareTokenType(Token.Type.If)) {
            Token token = scanner.peek();
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            Expression orCondition = tryOrCondition();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            Block ifBlock = tryBlock();

            if (compareTokenType(Token.Type.Else)) {
                scanner.next();
                Block elseBlock = tryBlock();

                return new Conditional(orCondition, ifBlock, elseBlock, token.getPosition());
            }
            return new Conditional(orCondition, ifBlock, token.getPosition());
        }
        return null;
    }

    public Loop tryLoop() throws Exception {
        if (compareTokenType(Token.Type.Foreach)) {
            Token token = scanner.peek();
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            if (compareTokenType(Token.Type.VarType) || compareTokenType(Token.Type.Type)) {
                Token typeOrVar = scanner.get();

                checkCurrentToken(Token.Type.Identifier);
                Token variableIdentifier = scanner.get();

                checkCurrentToken(Token.Type.Colon);

                checkNextToken(Token.Type.Identifier);
                Token arrayIdentifier = scanner.get();

                checkCurrentToken(Token.Type.ParenthesisRight);
                scanner.next();

                Block block = tryBlock();
                return new Loop(keywordToVariable(typeOrVar.getValue(), variableIdentifier.getValue()), arrayIdentifier.getValue(), block, token.getPosition());
            }
            throw new ParserException(scanner.peek(), Token.Type.Type);
        }
        return null;
    }

    public BoardChange tryBoardChange() throws Exception {
        if (compareTokenType(Token.Type.Player)) {
            Token token = scanner.peek();
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            Value player = tryValue();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            PlayerAction playerAction = tryPlayerAction();

            BoardChange boardChange = new BoardChange(player, playerAction, token.getPosition());

            checkCurrentToken(Token.Type.Semicolon);
            scanner.next();

            return boardChange;
        }
        return null;
    }

    public PlayerAction tryPlayerAction() throws Exception {
        PlayerAction playerAction;
        if ((playerAction = tryUnitsAction()) != null) {
            return playerAction;
        }
        if ((playerAction = tryActivation()) != null) {
            return playerAction;
        }
        return null;
    }

    public UnitsAction tryUnitsAction() throws Exception {
        if (compareTokenType(Token.Type.Move) || compareTokenType(Token.Type.Add) || compareTokenType(Token.Type.Remove)) {
            Token unitsAction = scanner.get();
            UnitsList unitsList = tryUnitsList();
            Value fromWhere = null;
            if (unitsAction.getType() == Token.Type.Move || unitsAction.getType() == Token.Type.Remove) {
                checkCurrentToken(Token.Type.From);
                scanner.next();
                checkCurrentToken(Token.Type.ParenthesisLeft);
                scanner.next();

                fromWhere = tryValue();

                checkCurrentToken(Token.Type.ParenthesisRight);
                scanner.next();
            }
            Value toWhere = null;
            if (unitsAction.getType() == Token.Type.Move || unitsAction.getType() == Token.Type.Add) {
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
        while ((unit = tryValue()) != null) {
            checkCurrentToken(Token.Type.Colon);
            scanner.next();

            amount = tryValue();

            unitsList.add(unit, amount);

            if (!compareTokenType(Token.Type.Comma)) {
                break;
            }
            scanner.next();
        }

        checkCurrentToken(Token.Type.ParenthesisRight);
        scanner.next();

        return unitsList;
    }

    public Activation tryActivation() throws Exception {
        if (compareTokenType(Token.Type.Activate) || compareTokenType(Token.Type.Deactivate)) {
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
        if (compareTokenType(Token.Type.Identifier)) {
            Token identifier = scanner.get();
            if (compareTokenType(Token.Type.ParenthesisLeft)) {
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

        return new FunctionCall(identifier.getValue(), arguments, identifier.getPosition());
    }

    public Arguments tryArguments() throws Exception {
        Arguments arguments = new Arguments();
        Expression orCondition;
        while ((orCondition = tryOrCondition()) != null) {
            arguments.add(orCondition);

            if (!compareTokenType(Token.Type.Comma)) {
                break;
            }
            scanner.next();
        }
        return arguments;
    }

    public Assignment tryAssign(Token identifier) throws Exception {
        String index = null;
        if (compareTokenType(Token.Type.BracketsLeft)) {
            checkNextToken(Token.Type.NumberLiteral);
            index = scanner.peek().getValue();
            checkNextToken(Token.Type.BracketsRight);
            scanner.next();
        }
        checkCurrentToken(Token.Type.Equals);
        scanner.next();

        Expression orCondition = tryOrCondition();
        if (orCondition == null) {
            throw new ParserException(scanner.peek(), "Wrong value for assignment.");
        }

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new Assignment(index, identifier.getValue(), orCondition, identifier.getPosition());
    }

    public Statement tryVarOrArrayDeclaration() throws Exception {
        if (compareTokenType(Token.Type.Type) || compareTokenType(Token.Type.VarType)) {
            Token type = scanner.get();
            if (compareTokenType(Token.Type.BracketsLeft)) {
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

        Expression orCondition = tryOrCondition();
        if (orCondition == null) {
            throw new ParserException(scanner.peek(), "Missing value for variable declaration");
        }

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new VariableDeclaration(keywordToVariable(type.getValue(), identifier.getValue()), orCondition, identifier.getPosition());
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

        checkCurrentToken(Token.Type.BracketsLeft);

        checkNextToken(Token.Type.NumberLiteral);

        int size = Integer.parseInt(scanner.get().getValue());
        ArrayDeclaration arrayDeclaration = new ArrayDeclaration(new ArrayVariable(keywordToVariable(arrayType.getValue(), identifier.getValue()), size), identifier.getPosition());
        arrayDeclaration.setSize(size);

        checkCurrentToken(Token.Type.BracketsRight);
        scanner.next();

        if (compareTokenType(Token.Type.BracesLeft)) {
            scanner.next();
            Value value;
            while ((value = tryValue()) != null) {
                arrayDeclaration.add(value);
                if (!compareTokenType(Token.Type.Comma)) {
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
        switch (type) {
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
                break;
        }
        throw new ParserException(scanner.peek(), "Unknown variable type.");
    }

    public Expression tryOrCondition() throws Exception {
        OrCondition orCondition = new OrCondition();

        Expression andCondition = tryAndCondition();

        if (andCondition == null) {
            return null;
        }

        orCondition.add(andCondition);
        while (scanner.peek().getType() == Token.Type.Or) {
            scanner.next();
            andCondition = tryAndCondition();

            if (andCondition == null) {
                throw new ParserException(scanner.peek(), "Wrong expression.");
            }

            orCondition.add(andCondition);
        }
        if (orCondition.getAndConditions().size() > 1) {
            return orCondition;
        } else {
            return andCondition;
        }
    }

    public Expression tryAndCondition() throws Exception {
        AndCondition andCondition = new AndCondition();

        Expression equalityCondition = tryEqualityCondition();

        if (equalityCondition == null) {
            return null;
        }

        andCondition.add(equalityCondition);
        while (scanner.peek().getType() == Token.Type.And) {
            scanner.next();
            equalityCondition = tryEqualityCondition();

            if (equalityCondition == null) {
                throw new ParserException(scanner.peek(), "Wrong expression.");
            }

            andCondition.add(equalityCondition);
        }
        if (andCondition.getEqualityCondition().size() > 1) {
            return andCondition;
        } else {
            return equalityCondition;
        }
    }

    public Expression tryEqualityCondition() throws Exception {
        EqualityCondition equalityCondition = new EqualityCondition();

        Expression relationalCondition = tryRelationalCondition();

        if (relationalCondition == null) {
            return null;
        }

        equalityCondition.add(relationalCondition);

        Token token;

        while (scanner.peek().getType() == Token.Type.And) {
            token = scanner.get();
            relationalCondition = tryRelationalCondition();

            if (relationalCondition == null) {
                throw new ParserException(scanner.peek(), "Wrong expression.");
            }

            equalityCondition.add(tokenTypeToOperator(token.getType()), relationalCondition);
        }
        if (equalityCondition.getRelationalConditions().size() > 1) {
            return equalityCondition;
        } else {
            return relationalCondition;
        }
    }

    public Expression tryRelationalCondition() throws Exception {
        RelationalCondition relationalCondition = new RelationalCondition();

        Expression addExpression = tryAddExpression();

        if (addExpression == null) {
            return null;
        }

        relationalCondition.add(addExpression);

        Token token;

        while (isRelation(scanner.peek().getType())) {
            token = scanner.get();
            addExpression = tryAddExpression();

            if (addExpression == null) {
                throw new ParserException(scanner.peek(), "Wrong expression.");
            }

            relationalCondition.add(tokenTypeToOperator(token.getType()), addExpression);
        }
        if (relationalCondition.getAddExpressions().size() > 1) {
            return relationalCondition;
        } else {
            return addExpression;
        }
    }

    public boolean isRelation(Token.Type type) {
        return type == Token.Type.Greater || type == Token.Type.Less || type == Token.Type.LessEqual || type == Token.Type.GreaterEqual || type == Token.Type.Equal || type == Token.Type.NotEqual;
    }

    public Expression tryAddExpression() throws Exception {
        AddExpression addExpression = new AddExpression();

        Expression multiplyExpression = tryMultiplyExpression();

        if (multiplyExpression == null) {
            return null;
        }

        addExpression.add(multiplyExpression);

        Token token;

        while (isAdd(scanner.peek().getType())) {
            token = scanner.get();

            multiplyExpression = tryMultiplyExpression();

            if (multiplyExpression == null) {
                throw new ParserException(scanner.peek(), "Wrong expression.");
            }

            addExpression.add(tokenTypeToOperator(token.getType()), multiplyExpression);
        }
        if (addExpression.getMultiplyExpressions().size() > 1) {
            return addExpression;
        } else {
            return multiplyExpression;
        }
    }

    public boolean isAdd(Token.Type type) {
        return type == Token.Type.Plus || type == Token.Type.Minus;
    }

    public Expression tryMultiplyExpression() throws Exception {
        MultiplyExpression multiplyExpression = new MultiplyExpression();

        Expression expression = tryUnaryExpression();

        if (expression == null) {
            return null;
        }

        multiplyExpression.add(expression);

        Token token;
        while (isMultiply(scanner.peek().getType())) {
            token = scanner.get();

            expression = tryUnaryExpression();

            if (expression == null) {
                throw new ParserException(scanner.peek(), "Wrong expression.");
            }

            multiplyExpression.add(tokenTypeToOperator(token.getType()), expression);
        }
        if (multiplyExpression.getExpressions().size() > 1) {
            return multiplyExpression;
        } else {
            return expression;
        }
    }

    public boolean isMultiply(Token.Type type) {
        return type == Token.Type.Multiply || type == Token.Type.Divide;
    }

    public Expression tryUnaryExpression() throws Exception {
        Expression expression;
        if ((expression = tryNegativeUnaryExpression()) != null || (expression = tryNotUnaryExpression()) != null) {
            return new UnaryExpression(expression);
        }
        expression = tryExpression();
        if (expression == null) {
            return null;
        }
        return new UnaryExpression(expression);
    }

    public Expression tryNegativeUnaryExpression() throws Exception {
        if (compareTokenType(Token.Type.Minus)) {
            scanner.next();
            Expression unaryExpression = tryUnaryExpression();
            return new NegativeUnaryExpression(unaryExpression);
        }
        return null;
    }

    public Expression tryNotUnaryExpression() throws Exception {
        if (compareTokenType(Token.Type.Not)) {
            scanner.next();
            Expression unaryExpression = tryUnaryExpression();
            return new NotUnaryExpression(unaryExpression);
        }
        return null;
    }

    public Expression tryExpression() throws Exception {
        if (compareTokenType(Token.Type.ParenthesisLeft)) {
            scanner.next();

            Expression orCondition = tryOrCondition();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            return orCondition;
        } else {
            return tryValue();
        }
    }

    public Operator tokenTypeToOperator(Token.Type type) throws ParserException {
        switch (type) {
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
        throw new ParserException(scanner.peek(), "Unknown operator type.");
    }

    private boolean isBoardCheck(Token.Type type) {
        return type == Token.Type.Player || type == Token.Type.Type;
    }

    public Value tryValue() throws Exception {
        Token token = scanner.peek();
        if (isLiteral(scanner.peek().getType())) {
            scanner.next();
            return tokenTypeToLiteral(token.getType(), token.getValue());
        }
        if (token.getType() == Token.Type.Identifier) {
            Token identifier = scanner.get();
            if (compareTokenType(Token.Type.ParenthesisLeft)) {
                return tryFunctionCallValue(identifier);
            }
            if (compareTokenType(Token.Type.BracketsLeft)) {
                scanner.next();

                Value value = tryValue();

                checkCurrentToken(Token.Type.BracketsRight);
                scanner.next();

                return new VariableValue(identifier.getValue(), value);
            }
            return new VariableValue(identifier.getValue());
        }
        if (isBoardCheck(token.getType())) {
            return tryBoardStateCheck();
        }
        return null;
    }

    private boolean isLiteral(Token.Type type) {
        return type == Token.Type.NumberLiteral || type == Token.Type.StringLiteral || type == Token.Type.BoolLiteral || type == Token.Type.UnitLiteral ||
                type == Token.Type.ColorLiteral || type == Token.Type.HexLiteral || type == Token.Type.PlanetLiteral;
    }

    public Literal tokenTypeToLiteral(Token.Type type, String value) throws ParserException {
        switch (type) {
            case NumberLiteral:
                return new IntLiteral(value);
            case StringLiteral:
                return new StringLiteral(value.substring(1, value.length() - 1));
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
                break;
        }
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
        if (scanner.peek().getType() == Token.Type.Player) {
            return tryPlayerStateCheckOrActivationCheck();
        } else {
            return tryPlanetOrHexStateCheck();
        }
    }

    public Value tryPlanetOrHexStateCheck() throws Exception {
        if (compareTokenType(Token.Type.Type) && (scanner.peek().getValue().equals("hex") || scanner.peek().getValue().equals("planet"))) {
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
            if (type.getValue().equals("planet")) {
                return new PlanetStateCheck(place, unit);
            } else if (type.getValue().equals("hex")) {
                return new HexStateCheck(place, unit);
            }
        }
        return null;
    }

    public Value tryPlayerStateCheckOrActivationCheck() throws Exception {
        if (compareTokenType(Token.Type.Player)) {
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            Value player = tryValue();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            if (compareTokenType(Token.Type.Has)) {
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
            } else if (compareTokenType(Token.Type.Activated)) {
                checkNextToken(Token.Type.ParenthesisLeft);
                scanner.next();
                Value hex = tryValue();
                checkCurrentToken(Token.Type.ParenthesisRight);
                scanner.next();
                return new ActivationCheck(player, hex);
            } else {
                throw new ParserException(scanner.peek(), "Expected: \"has\" or \"activated\"");
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
        if (!compareTokenType(type)) {
            throw new ParserException(token, type);
        }
    }

    private boolean compareTokenType(Token.Type type) {
        return scanner.peek().getType() == type;
    }
}