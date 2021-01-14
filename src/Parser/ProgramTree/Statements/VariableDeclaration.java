package Parser.ProgramTree.Statements;

import Parser.ProgramTree.ConditionExpresion.ConditionExpression;
import Parser.ProgramTree.Variables.Variable;

public class VariableDeclaration extends Statement{
    private final Variable variable;
    private final ConditionExpression value;
    public VariableDeclaration(Variable variable, ConditionExpression value){
        this.variable = variable;
        this.value = value;
    }

    public Variable getVariable() {
        return variable;
    }

    public ConditionExpression getValue() {
        return value;
    }
}
