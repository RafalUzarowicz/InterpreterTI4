package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.OrCondition;

import java.util.ArrayList;

public class Arguments {
    private ArrayList<OrCondition> arguments;
    public Arguments(){
        arguments = new ArrayList<>();
    }
    public void add(OrCondition orCondition){
        arguments.add(orCondition);
    }
}
