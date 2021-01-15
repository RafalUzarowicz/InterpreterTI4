package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;

public class NotUnaryExpression extends Expression implements INode {
    UnaryExpression unaryExpression;
    public NotUnaryExpression(UnaryExpression unaryExpression){
        this.unaryExpression = unaryExpression;
    }

    @Override
    public String toString() {
        return "!" + unaryExpression.toString();
    }
}
