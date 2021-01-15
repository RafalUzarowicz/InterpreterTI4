package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.INode;

public class Return extends Statement implements INode {
    private final OrCondition returnValue;
    public Return(OrCondition conditionExpression){
        returnValue = conditionExpression;
    }

    public OrCondition getReturnValue() {
        return returnValue;
    }
}
