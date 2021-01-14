package Parser.ProgramTree.ConditionExpresion;

public class NotUnaryExpression extends Expression{
    UnaryExpression unaryExpression;
    public NotUnaryExpression(UnaryExpression unaryExpression){
        this.unaryExpression = unaryExpression;
    }

    @Override
    public String toString() {
        return "!" + unaryExpression.toString();
    }
}
