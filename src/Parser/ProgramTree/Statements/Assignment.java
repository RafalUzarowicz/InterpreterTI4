package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.ConditionExpression;

public class Assignment extends Statement{
    private String index;
    private String identifier;
    private ConditionExpression value;
    public Assignment(String index, String identifier, ConditionExpression value){
        this.index = index;
        this.identifier = identifier;
        this.value = value;
    }
}
