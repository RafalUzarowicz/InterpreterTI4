package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;

public class UnaryExpression implements INode {
    private Expression expression;
    public UnaryExpression(Expression expression){
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
