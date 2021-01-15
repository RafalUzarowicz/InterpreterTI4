package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.Variables.Variable;

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
