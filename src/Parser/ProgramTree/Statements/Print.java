package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.OrCondition;

import java.util.ArrayList;

public class Print extends Statement{
    ArrayList<OrCondition> orConditions;
    public Print(){
        orConditions = new ArrayList<>();
    }
    public void add(OrCondition orCondition){
        orConditions.add(orCondition);
    }
}
