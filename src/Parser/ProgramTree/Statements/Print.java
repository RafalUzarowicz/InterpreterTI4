package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.OrCondition;

import java.util.ArrayList;

public class Print extends Statement{
    ArrayList<OrCondition> conditionExpressions;
    public Print(){
        conditionExpressions = new ArrayList<>();
    }
    public void add(OrCondition conditionExpression){
        conditionExpressions.add(conditionExpression);
    }
}
