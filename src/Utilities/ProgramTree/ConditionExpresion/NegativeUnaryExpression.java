package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class NegativeUnaryExpression extends Expression implements INode {
    private final UnaryExpression unaryExpression;
    public NegativeUnaryExpression(UnaryExpression unaryExpression){
        this.unaryExpression =unaryExpression;
    }

    public UnaryExpression getUnaryExpression() {
        return unaryExpression;
    }

    @Override
    public String toString() {
        return "-(" + unaryExpression.toString() +")";
    }
}
