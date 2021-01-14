package Utilities;

import Parser.ProgramTree.ConditionExpresion.OperatorType;
import Parser.ProgramTree.ConditionExpresion.Operators.*;
import Scanner.Token;

import java.util.HashMap;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class with useful maps for Parser.
 */
public class ParserUtils {
    public final static HashMap<Class, Integer> operatorPriority = new HashMap<>() {{
        put(OrOperator.class, 1);

        put(AndOperator.class, 2);

        put(GreaterOperator.class, 3);
        put(GreaterEqualOperator.class, 3);
        put(LessOperator.class, 3);
        put(LessEqualOperator.class, 3);
        put(EqualOperator.class, 3);
        put(NotEqualOperator.class, 3);

        put(NotOperator.class, 8);

        put(PlusOperator.class, 5);
        put(MinusOperator.class, 5);

        put(MultiplyOperator.class, 6);
        put(DivideOperator.class, 6);

        put(NegativeOperator.class, 7);
    }};
    public final static HashMap<Class, Integer> operatorOperandsNumber = new HashMap<>() {{
        put(OrOperator.class, 2);

        put(AndOperator.class, 2);

        put(GreaterOperator.class, 2);
        put(GreaterEqualOperator.class, 2);
        put(LessOperator.class, 2);
        put(LessEqualOperator.class, 2);
        put(EqualOperator.class, 2);
        put(NotEqualOperator.class, 2);

        put(NotOperator.class, 1);

        put(PlusOperator.class, 2);
        put(MinusOperator.class, 2);

        put(MultiplyOperator.class, 2);
        put(DivideOperator.class, 2);

        put(NegativeOperator.class, 1);
    }};

    public final static HashMap<OperatorType, String> operatorToString = new HashMap<>() {{
        put(OperatorType.Or, "||");

        put(OperatorType.And, "&&");

        put(OperatorType.Greater, ">");
        put(OperatorType.GreaterEqual, ">=");
        put(OperatorType.Less, "<");
        put(OperatorType.LessEqual, "<=");
        put(OperatorType.Equal, "==");
        put(OperatorType.NotEqual, "!=");

        put(OperatorType.Not, "!");

        put(OperatorType.Plus, "+");
        put(OperatorType.Minus, "-");

        put(OperatorType.Multiply, "*");
        put(OperatorType.Divide, "/");

        put(OperatorType.Negative, "-");
    }};

    public final static HashMap<Token.Type, OperatorType> forConditionExpression = new HashMap<>() {{
        put(Token.Type.Or, OperatorType.Or);

        put(Token.Type.And, OperatorType.And);

        put(Token.Type.Greater, OperatorType.Greater);
        put(Token.Type.GreaterEqual, OperatorType.GreaterEqual);
        put(Token.Type.Less, OperatorType.Less);
        put(Token.Type.LessEqual, OperatorType.LessEqual);
        put(Token.Type.Equal, OperatorType.Equal);
        put(Token.Type.NotEqual, OperatorType.NotEqual);

        put(Token.Type.Not, OperatorType.Not);

        put(Token.Type.Plus, OperatorType.Plus);
        put(Token.Type.Minus, OperatorType.Minus);

        put(Token.Type.Multiply, OperatorType.Multiply);
        put(Token.Type.Divide, OperatorType.Divide);
    }};
    public static boolean compareOperators(Class first, Class second){
        int firstPriority = operatorPriority.get(first) != null ? operatorPriority.get(first) : 0;
        int secondPriority = operatorPriority.get(second) != null ? operatorPriority.get(second) : 0;
        return firstPriority >= secondPriority;
    }
}
