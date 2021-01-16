package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.INode;

import java.util.ArrayList;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Print extends Statement implements INode {
    ArrayList<OrCondition> conditionExpressions;
    public Print(){
        conditionExpressions = new ArrayList<>();
    }
    public void add(OrCondition conditionExpression){
        conditionExpressions.add(conditionExpression);
    }

    public ArrayList<OrCondition> getConditionExpressions() {
        return conditionExpressions;
    }
}
