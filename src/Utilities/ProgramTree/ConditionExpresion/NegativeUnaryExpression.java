package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;

public class NegativeUnaryExpression extends Expression implements INode {
    UnaryExpression unaryExpression;
    public NegativeUnaryExpression(UnaryExpression unaryExpression){
        this.unaryExpression =unaryExpression;
    }

    @Override
    public String toString() {
        return "-" + unaryExpression.toString();
    }
}
