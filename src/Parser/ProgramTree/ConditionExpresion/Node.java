package Parser.ProgramTree.ConditionExpresion;

import Parser.ProgramTree.Value.Value;

public class Node {
    public enum Operator {
        Or,
        And,
        Greater,
        GreaterEqual,
        Less,
        LessEqual,
        Equal,
        NotEqual,
        Not,
        Plus,
        Minus,
        Multiply,
        Divide,
        Negative,
        Non
    }

    public enum Parenthesis{
        Left,
        Right,
        Non
    }

    private Value value;
    public Operator operator;
    public Parenthesis parenthesis;
    public Node left, right;
    public Node child;

    public Node(Value value) {
        this.value = value;
        operator = Operator.Non;
        parenthesis = Parenthesis.Non;
        left = right = child = null;
    }

    public Node(Operator operator) {
        this.operator = operator;
        parenthesis = Parenthesis.Non;
        value = null;
        left = right = child = null;
    }

    public Node(Parenthesis parenthesis) {
        this.parenthesis = parenthesis;
        operator = Operator.Non;
        value = null;
        left = right = child = null;
    }
}
