package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.ConditionExpresion.OrCondition;

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
