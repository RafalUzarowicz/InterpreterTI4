package Parser.ProgramTree.ConditionExpresion;

import java.util.ArrayList;

public class AndCondition {
    ArrayList<RelationalCondition> relationalConditions;
    public AndCondition(){
        relationalConditions = new ArrayList<>();
    }

    public void add(RelationalCondition relationalCondition){
        relationalConditions.add(relationalCondition);
    }
}
