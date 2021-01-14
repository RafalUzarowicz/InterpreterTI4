package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.OrCondition;
import Parser.ProgramTree.Variables.Variable;

public class VariableDeclaration extends Statement{
    private final Variable variable;
    private final OrCondition value;
    public VariableDeclaration(Variable variable, OrCondition value){
        this.variable = variable;
        this.value = value;
    }

    public Variable getVariable() {
        return variable;
    }

    public OrCondition getValue() {
        return value;
    }
}
