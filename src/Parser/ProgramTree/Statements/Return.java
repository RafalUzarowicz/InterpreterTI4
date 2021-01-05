package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.OrCondition;

public class Return extends Statement{
    private final OrCondition returnValue;
    public Return(){
        returnValue = null;
    }
    public Return(OrCondition orCondition){
        returnValue = orCondition;
    }
}
