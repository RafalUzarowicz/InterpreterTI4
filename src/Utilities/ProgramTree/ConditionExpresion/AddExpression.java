package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.ConditionExpresion.Operators.Operator;
import Utilities.ProgramTree.INode;

import java.util.ArrayList;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class AddExpression extends Expression implements INode {
    private final ArrayList<Expression> multiplyExpressions;
    private final ArrayList<Operator> operators;

    public AddExpression() {
        multiplyExpressions = new ArrayList<>();
        operators = new ArrayList<>();
    }

    public void add(Expression multiplyExpression) {
        multiplyExpressions.add(multiplyExpression);
    }

    public void add(Operator operator, Expression multiplyExpression) {
        multiplyExpressions.add(multiplyExpression);
        operators.add(operator);
    }

    public ArrayList<Expression> getMultiplyExpressions() {
        return multiplyExpressions;
    }

    public ArrayList<Operator> getOperators() {
        return operators;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (multiplyExpressions.size() > 1)
            stringBuilder.append("(");
        stringBuilder.append(multiplyExpressions.get(0).toString());

        for (int i = 1; i < multiplyExpressions.size(); ++i) {
            stringBuilder.append(operators.get(i - 1).toString());
            stringBuilder.append(multiplyExpressions.get(i).toString());
        }
        if (multiplyExpressions.size() > 1)
            stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
