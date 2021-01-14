package Parser.ProgramTree.ConditionExpresion;

import java.util.ArrayList;

public class RelationalCondition {
    ArrayList<AddExpression> addExpressions;
    public RelationalCondition(){
        addExpressions = new ArrayList<>();
    }
    public void add(AddExpression addExpression){
        addExpressions.add(addExpression);
    }
}
