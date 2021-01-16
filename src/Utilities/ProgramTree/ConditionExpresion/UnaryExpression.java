package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class UnaryExpression implements INode {
    private final Expression expression;
    public UnaryExpression(Expression expression){
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
