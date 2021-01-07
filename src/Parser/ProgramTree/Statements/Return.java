package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.ConditionExpression;

public class Return extends Statement{
    private final ConditionExpression returnValue;
    public Return(){
        returnValue = null;
    }
    public Return(ConditionExpression conditionExpression){
        returnValue = conditionExpression;
    }
}
