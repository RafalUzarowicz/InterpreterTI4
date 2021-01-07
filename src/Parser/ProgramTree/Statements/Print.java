package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.ConditionExpression;

import java.util.ArrayList;

public class Print extends Statement{
    ArrayList<ConditionExpression> conditionExpressions;
    public Print(){
        conditionExpressions = new ArrayList<>();
    }
    public void add(ConditionExpression conditionExpression){
        conditionExpressions.add(conditionExpression);
    }
}
