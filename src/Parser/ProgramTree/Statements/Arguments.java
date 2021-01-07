package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.ConditionExpression;

import java.util.ArrayList;

public class Arguments {
    private ArrayList<ConditionExpression> arguments;
    public Arguments(){
        arguments = new ArrayList<>();
    }
    public void add(ConditionExpression conditionExpression){
        arguments.add(conditionExpression);
    }
}
