package Parser.ProgramTree.ConditionExpresion;

public class ConditionExpression {

    private final Node expression;
    public ConditionExpression(Node root){
        this.expression = root;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
