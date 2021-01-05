package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.OrCondition;
import Parser.ProgramTree.Variable;

public class VariableDeclaration extends Statement{
    private Variable variable;
    private OrCondition value;
    public VariableDeclaration(Variable variable, OrCondition value){
        this.variable = variable;
        this.value = value;
    }
}
