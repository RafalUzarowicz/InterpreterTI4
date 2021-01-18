package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.INode;

import java.util.ArrayList;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class OrCondition extends Expression implements INode {
    private final ArrayList<Expression> andConditions;
    public OrCondition(){
        andConditions = new ArrayList<>();
    }

    public void add(Expression andCondition){
        andConditions.add(andCondition);
    }

    public ArrayList<Expression> getAndConditions() {
        return andConditions;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(andConditions.size() > 1)
            stringBuilder.append("(");
        stringBuilder.append(andConditions.get(0).toString());

        for(int i = 1; i<andConditions.size(); ++i){
            stringBuilder.append("||");
            stringBuilder.append(andConditions.get(i).toString());
        }
        if(andConditions.size() > 1)
            stringBuilder.append(")");

        return stringBuilder.toString();
    }
}
