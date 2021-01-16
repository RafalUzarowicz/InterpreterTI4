package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;

import java.util.ArrayList;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class AndCondition implements INode {
    private final ArrayList<EqualityCondition> equalityConditions;
    public AndCondition(){
        equalityConditions = new ArrayList<>();
    }

    public void add(EqualityCondition equalityCondition){
        equalityConditions.add(equalityCondition);
    }

    public ArrayList<EqualityCondition> getEqualityCondition() {
        return equalityConditions;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(equalityConditions.get(0).toString());

        for(int i = 1; i<equalityConditions.size(); ++i){
            stringBuilder.append("&&");
            stringBuilder.append(equalityConditions.get(i).toString());
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

}
