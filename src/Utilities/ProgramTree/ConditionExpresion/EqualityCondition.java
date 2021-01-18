package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.ConditionExpresion.Operators.Operator;
import Utilities.ProgramTree.INode;

import java.util.ArrayList;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class EqualityCondition extends Expression implements INode {
    private final ArrayList<Expression> relationalConditions;
    private final ArrayList<Operator> operators;

    public EqualityCondition() {
        relationalConditions = new ArrayList<>();
        operators = new ArrayList<>();
    }

    public void add(Expression relationalCondition) {
        relationalConditions.add(relationalCondition);
    }

    public void add(Operator operator, Expression relationalCondition) {
        operators.add(operator);
        relationalConditions.add(relationalCondition);
    }

    public ArrayList<Expression> getRelationalConditions() {
        return relationalConditions;
    }

    public ArrayList<Operator> getOperators() {
        return operators;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (relationalConditions.size() > 1)
            stringBuilder.append("(");
        stringBuilder.append(relationalConditions.get(0).toString());

        for (int i = 1; i < relationalConditions.size(); ++i) {
            stringBuilder.append(operators.get(i - 1).toString());
            stringBuilder.append(relationalConditions.get(i).toString());
        }
        if (relationalConditions.size() > 1)
            stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
