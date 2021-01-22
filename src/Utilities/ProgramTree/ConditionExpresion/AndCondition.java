package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;

import java.util.ArrayList;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class AndCondition extends Expression implements INode {
    private final ArrayList<Expression> equalityConditions;

    public AndCondition() {
        equalityConditions = new ArrayList<>();
    }

    public void add(Expression equalityCondition) {
        equalityConditions.add(equalityCondition);
    }

    public ArrayList<Expression> getEqualityCondition() {
        return equalityConditions;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (equalityConditions.size() > 1)
            stringBuilder.append("(");
        stringBuilder.append(equalityConditions.get(0).toString());

        for (int i = 1; i < equalityConditions.size(); ++i) {
            stringBuilder.append("&&");
            stringBuilder.append(equalityConditions.get(i).toString());
        }
        if (equalityConditions.size() > 1)
            stringBuilder.append(")");
        return stringBuilder.toString();
    }

}
