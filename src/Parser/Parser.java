package Parser;

import Exceptions.ParserException;
import Parser.ProgramTree.*;
import Parser.ProgramTree.BoardChange.*;
import Parser.ProgramTree.ConditionExpresion.OrCondition;
import Parser.ProgramTree.Statements.*;
import Scanner.*;
import Utilities.ParserKeywords;

import java.util.ArrayList;

public class Parser {
    private final Scanner scanner;

    public Parser(Scanner scanner){
        this.scanner = scanner;
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

        return new Function(new Variable(ParserKeywords.keywordToVariableType.get(type.getValue())), identifier.getValue(), parameters, block);
    }

    private Parameters tryParameters() throws Exception {
        Parameters parameters = new Parameters();
        while(compareTokenType(Token.Type.Type)){
            Token type = scanner.get();

            checkCurrentToken(Token.Type.Identifier);
            Token identifier = scanner.get();

            parameters.add(new Variable(ParserKeywords.keywordToVariableType.get(type.getValue()), identifier.getValue()));

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

            OrCondition orCondition;
            while((orCondition = tryOrCondition()) != null){
                print.add(orCondition);
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
            if(compareTokenType(Token.Type.Semicolon)){
                scanner.next();
                return new Return();
            }
            OrCondition orCondition = tryOrCondition();
            checkCurrentToken(Token.Type.Semicolon);
            scanner.next();
            return new Return(orCondition);
        }
        return null;
    }

    private Conditional tryConditional() throws Exception {
        if(compareTokenType(Token.Type.If)){
            checkNextToken(Token.Type.ParenthesisLeft);
            scanner.next();

            OrCondition orCondition = tryOrCondition();

            checkCurrentToken(Token.Type.ParenthesisRight);
            scanner.next();

            Block ifBlock = tryBlock();

            if(compareTokenType(Token.Type.Else)){
                scanner.next();
                Block elseBlock = tryBlock();

                return new Conditional(orCondition, ifBlock, elseBlock);
            }
            return new Conditional(orCondition, ifBlock);
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
                return new Loop(new Variable(ParserKeywords.keywordToVariableType.get(typeOrVar.getValue()), variableIdentifier.getValue()), arrayIdentifier.getValue(), block);
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
                return tryFunctionCall(identifier);
            }
            return tryAssign(identifier);
        }
        return null;
    }

    private FunctionCall tryFunctionCall(Token identifier) throws Exception {
        checkCurrentToken(Token.Type.ParenthesisLeft);
        scanner.next();

        Arguments arguments = tryArguments();

        checkCurrentToken(Token.Type.ParenthesisRight);
        scanner.next();

        return new FunctionCall(identifier.getValue(), arguments);
    }

    private Arguments tryArguments() throws Exception {
        Arguments arguments = new Arguments();
        OrCondition orCondition;
        while((orCondition = tryOrCondition()) != null){
            arguments.add(orCondition);

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

        OrCondition orCondition = tryOrCondition();

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new Assignment(index, identifier.getValue(), orCondition);
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

        OrCondition orCondition = tryOrCondition();

        checkCurrentToken(Token.Type.Semicolon);
        scanner.next();

        return new VariableDeclaration(new Variable(ParserKeywords.keywordToVariableType.get(type.getValue()), identifier.getValue()), orCondition);
    }

    private ArrayDeclaration tryArrayDeclaration(Token type) throws Exception {
        checkCurrentToken(Token.Type.BracketsLeft);
        checkNextToken(Token.Type.BracketsRight);

        checkCurrentToken(Token.Type.Identifier);
        Token identifier = scanner.get();

        checkCurrentToken(Token.Type.Equals);
        scanner.next();

        checkCurrentToken(Token.Type.Type);
        Token arrayType = scanner.get();

        ArrayDeclaration arrayDeclaration = new ArrayDeclaration(ParserKeywords.keywordToVariableType.get(arrayType.getValue()), identifier.getValue());

        checkCurrentToken(Token.Type.BracketsLeft);
        scanner.next();

        if(compareTokenType(Token.Type.NumberLiteral)){
            arrayDeclaration.setSize(Integer.getInteger(scanner.get().getValue()));
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

    private OrCondition tryOrCondition(){
        // TODO: orCondition
        return null;
    }

    private Value tryValue(){
        // TODO: value
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
