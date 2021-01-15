package Utilities.ProgramTree.ConditionExpresion;

public class NegativeUnaryExpression extends Expression{
    UnaryExpression unaryExpression;
    public NegativeUnaryExpression(UnaryExpression unaryExpression){
        this.unaryExpression =unaryExpression;
    }

    @Override
    public String toString() {
        return "-" + unaryExpression.toString();
    }
}
