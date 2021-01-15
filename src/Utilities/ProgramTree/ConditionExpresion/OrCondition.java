package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;

import java.util.ArrayList;

public class OrCondition extends Expression implements INode {
    ArrayList<AndCondition> andConditions;
    public OrCondition(){
        andConditions = new ArrayList<>();
    }

    public void add(AndCondition andCondition){
        andConditions.add(andCondition);
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(andConditions.get(0).toString());

        for(int i = 1; i<andConditions.size(); ++i){
            stringBuilder.append("||");
            stringBuilder.append(andConditions.get(i).toString());
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}
