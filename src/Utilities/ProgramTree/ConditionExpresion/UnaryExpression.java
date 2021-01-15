package Utilities.ProgramTree.ConditionExpresion;

public class UnaryExpression {
    private Expression expression;
    public UnaryExpression(Expression expression){
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
