package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class NotUnaryExpression extends Expression implements INode {
    private final Expression unaryExpression;
    public NotUnaryExpression(Expression unaryExpression){
        this.unaryExpression = unaryExpression;
    }

    public Expression getUnaryExpression() {
        return unaryExpression;
    }

    @Override
    public String toString() {
        return "!(" + unaryExpression.toString() + ")";
    }
}
