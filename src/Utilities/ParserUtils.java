package Utilities;

import Parser.ProgramTree.ConditionExpresion.Node;
import Parser.ProgramTree.Value.Literal;
import Parser.ProgramTree.Variable;
import Scanner.Token;

import java.util.HashMap;

public class ParserUtils {
    public final static HashMap<String, Variable.VariableType> keywordToVariableType = new HashMap<>() {{
        put("int", Variable.VariableType.Int);
        put("string", Variable.VariableType.String);
        put("bool", Variable.VariableType.Bool);
        put("unit", Variable.VariableType.Unit);
        put("color", Variable.VariableType.Color);
        put("hex", Variable.VariableType.Hex);
        put("planet", Variable.VariableType.Planet);
        put("var", Variable.VariableType.Var);
    }};
    public final static HashMap<Token.Type, Literal.LiteralType> tokenTypeToLiteralType = new HashMap<>() {{
        put(Token.Type.NumberLiteral, Literal.LiteralType.Int);
        put(Token.Type.StringLiteral, Literal.LiteralType.String);
        put(Token.Type.BoolLiteral, Literal.LiteralType.Bool);
        put(Token.Type.UnitLiteral, Literal.LiteralType.Unit);
        put(Token.Type.ColorLiteral, Literal.LiteralType.Color);
        put(Token.Type.HexLiteral, Literal.LiteralType.Hex);
        put(Token.Type.PlanetLiteral, Literal.LiteralType.Planet);
    }};
    public final static HashMap<Node.Operator, Integer> operatorPriority = new HashMap<>() {{
        put(Node.Operator.Non, 0);

        put(Node.Operator.Or, 1);

        put(Node.Operator.And, 2);

        put(Node.Operator.Greater, 3);
        put(Node.Operator.GreaterEqual, 3);
        put(Node.Operator.Less, 3);
        put(Node.Operator.LessEqual, 3);
        put(Node.Operator.Equal, 3);
        put(Node.Operator.NotEqual, 3);

        put(Node.Operator.Not, 8);

        put(Node.Operator.Plus, 5);
        put(Node.Operator.Minus, 5);

        put(Node.Operator.Multiply, 6);
        put(Node.Operator.Divide, 6);

        put(Node.Operator.Negative, 7);
    }};
    public final static HashMap<Node.Operator, Integer> operatorOperandsNumber = new HashMap<>() {{
        put(Node.Operator.Or, 2);

        put(Node.Operator.And, 2);

        put(Node.Operator.Greater, 2);
        put(Node.Operator.GreaterEqual, 2);
        put(Node.Operator.Less, 2);
        put(Node.Operator.LessEqual, 2);
        put(Node.Operator.Equal, 2);
        put(Node.Operator.NotEqual, 2);

        put(Node.Operator.Not, 1);

        put(Node.Operator.Plus, 2);
        put(Node.Operator.Minus, 2);

        put(Node.Operator.Multiply, 2);
        put(Node.Operator.Divide, 2);

        put(Node.Operator.Negative, 1);
    }};

    public final static HashMap<Node.Operator, String> operatorToString = new HashMap<>() {{
        put(Node.Operator.Or, "||");

        put(Node.Operator.And, "&&");

        put(Node.Operator.Greater, ">");
        put(Node.Operator.GreaterEqual, ">=");
        put(Node.Operator.Less, "<");
        put(Node.Operator.LessEqual, "<=");
        put(Node.Operator.Equal, "==");
        put(Node.Operator.NotEqual, "!=");

        put(Node.Operator.Not, "!");

        put(Node.Operator.Plus, "+");
        put(Node.Operator.Minus, "-");

        put(Node.Operator.Multiply, "*");
        put(Node.Operator.Divide, "/");

        put(Node.Operator.Negative, "-");
    }};

    public final static HashMap<Token.Type, Node.Operator> forConditionExpression = new HashMap<>() {{
        put(Token.Type.Or, Node.Operator.Or);

        put(Token.Type.And, Node.Operator.And);

        put(Token.Type.Greater, Node.Operator.Greater);
        put(Token.Type.GreaterEqual, Node.Operator.GreaterEqual);
        put(Token.Type.Less, Node.Operator.Less);
        put(Token.Type.LessEqual,Node.Operator.LessEqual);
        put(Token.Type.Equal, Node.Operator.Equal);
        put(Token.Type.NotEqual, Node.Operator.NotEqual);

        put(Token.Type.Not, Node.Operator.Not);

        put(Token.Type.Plus, Node.Operator.Plus);
        put(Token.Type.Minus, Node.Operator.Minus);

        put(Token.Type.Multiply, Node.Operator.Multiply);
        put(Token.Type.Divide, Node.Operator.Divide);
    }};
    public static boolean compareOperators(Node.Operator first, Node.Operator second){
        return operatorPriority.get(first) >= operatorPriority.get(second);
    }
}
