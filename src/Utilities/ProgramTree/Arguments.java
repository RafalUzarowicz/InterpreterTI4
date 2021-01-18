package Utilities.ProgramTree;

import Utilities.ProgramTree.ConditionExpresion.Expression;
import Utilities.ProgramTree.ConditionExpresion.OrCondition;

import java.util.ArrayList;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Arguments  implements INode{
    private final ArrayList<Expression> arguments;

    public Arguments(){
        arguments = new ArrayList<>();
    }

    public void add(Expression conditionExpression){
        arguments.add(conditionExpression);
    }

    public ArrayList<Expression> getArguments() {
        return arguments;
    }

    public int getSize(){
        return arguments.size();
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
