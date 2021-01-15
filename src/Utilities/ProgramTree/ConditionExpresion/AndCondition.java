package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;

import java.util.ArrayList;

public class AndCondition implements INode {
    ArrayList<RelationalCondition> relationalConditions;
    public AndCondition(){
        relationalConditions = new ArrayList<>();
    }

    public void add(RelationalCondition relationalCondition){
        relationalConditions.add(relationalCondition);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(relationalConditions.get(0).toString());

        for(int i = 1; i<relationalConditions.size(); ++i){
            stringBuilder.append("&&");
            stringBuilder.append(relationalConditions.get(i).toString());
        }

        return stringBuilder.toString();
    }

}
