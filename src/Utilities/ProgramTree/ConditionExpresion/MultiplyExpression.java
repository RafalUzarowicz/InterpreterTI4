package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.ConditionExpresion.Operators.Operator;

import java.util.ArrayList;

public class MultiplyExpression {
    private final ArrayList<UnaryExpression> expressions;
    private final ArrayList<Operator> operators;
    public MultiplyExpression(){
        expressions = new ArrayList<>();
        operators = new ArrayList<>();
    }
    public void add(UnaryExpression expression){
        expressions.add(expression);
    }

    public void add(Operator operator, UnaryExpression expression){
        expressions.add(expression);
        operators.add(operator);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(expressions.get(0).toString());

        for(int i = 1; i<expressions.size(); ++i){
            stringBuilder.append(operators.get(i-1).toString());
            stringBuilder.append(expressions.get(i).toString());
        }

        return stringBuilder.toString();
    }
}
