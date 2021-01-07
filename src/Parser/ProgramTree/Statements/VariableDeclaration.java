package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.ConditionExpression;
import Parser.ProgramTree.Variable;

public class VariableDeclaration extends Statement{
    private Variable variable;
    private ConditionExpression value;
    public VariableDeclaration(Variable variable, ConditionExpression value){
        this.variable = variable;
        this.value = value;
    }
}
