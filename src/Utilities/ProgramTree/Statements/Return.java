package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.ConditionExpresion.OrCondition;

public class Return extends Statement{
    private final OrCondition returnValue;
    public Return(){
        returnValue = null;
    }
    public Return(OrCondition conditionExpression){
        returnValue = conditionExpression;
    }
}
