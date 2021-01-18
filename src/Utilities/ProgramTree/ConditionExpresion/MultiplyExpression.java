package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.ConditionExpresion.Operators.Operator;
import Utilities.ProgramTree.INode;

import java.util.ArrayList;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class MultiplyExpression extends Expression implements INode {
    private final ArrayList<Expression> expressions;
    private final ArrayList<Operator> operators;
    public MultiplyExpression(){
        expressions = new ArrayList<>();
        operators = new ArrayList<>();
    }
    public void add(Expression expression){
        expressions.add(expression);
    }

    public void add(Operator operator, Expression expression){
        expressions.add(expression);
        operators.add(operator);
    }

    public ArrayList<Expression> getExpressions() {
        return expressions;
    }

    public ArrayList<Operator> getOperators() {
        return operators;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(expressions.size() > 1)
            stringBuilder.append("(");
        stringBuilder.append(expressions.get(0).toString());

        for(int i = 1; i<expressions.size(); ++i){
            stringBuilder.append(operators.get(i-1).toString());
            stringBuilder.append(expressions.get(i).toString());
        }
        if(expressions.size() > 1)
            stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
