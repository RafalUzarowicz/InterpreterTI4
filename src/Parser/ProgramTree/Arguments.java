package Parser.ProgramTree;

import Parser.ProgramTree.ConditionExpresion.OrCondition;

import java.util.ArrayList;

public class Arguments {
    private final ArrayList<OrCondition> arguments;

    public Arguments(){
        arguments = new ArrayList<>();
    }

    public void add(OrCondition conditionExpression){
        arguments.add(conditionExpression);
    }

    public ArrayList<OrCondition> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int i = 0;
        for ( ; i<arguments.size()-1; ++i){
            str.append(arguments.get(i).toString());
            str.append(",");
        }
        if(i==arguments.size()-1){
            str.append(arguments.get(i).toString());
        }
        return str.toString();
    }
}
