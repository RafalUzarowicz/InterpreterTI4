package Parser.ProgramTree.ConditionExpresion;

import java.util.ArrayList;

public class OrCondition extends Expression{
    ArrayList<AndCondition> andConditions;
    public OrCondition(){
        andConditions = new ArrayList<>();
    }

    public void add(AndCondition andCondition){
        andConditions.add(andCondition);
    }
}
