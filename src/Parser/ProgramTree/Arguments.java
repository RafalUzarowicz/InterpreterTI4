package Parser.ProgramTree;

import Parser.ProgramTree.ConditionExpresion.ConditionExpression;

import java.util.ArrayList;

public class Arguments {
    public ArrayList<ConditionExpression> getArguments() {
        return arguments;
    }

    private final ArrayList<ConditionExpression> arguments;
    public Arguments(){
        arguments = new ArrayList<>();
    }
    public void add(ConditionExpression conditionExpression){
        arguments.add(conditionExpression);
    }
}
