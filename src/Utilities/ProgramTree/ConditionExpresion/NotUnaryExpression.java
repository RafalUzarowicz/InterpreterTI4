package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class NotUnaryExpression extends Expression implements INode {
    private final UnaryExpression unaryExpression;
    public NotUnaryExpression(UnaryExpression unaryExpression){
        this.unaryExpression = unaryExpression;
    }

    public UnaryExpression getUnaryExpression() {
        return unaryExpression;
    }

    @Override
    public String toString() {
        return "!(" + unaryExpression.toString() + ")";
    }
}
