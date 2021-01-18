package Utilities.ProgramTree.Statements;

import Utilities.Position;
import Utilities.ProgramTree.ConditionExpresion.Expression;
import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Variables.Variable;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class VariableDeclaration extends Statement implements INode {
    private final Variable variable;
    private final Expression value;

    public VariableDeclaration(Variable variable, Expression value, Position position) {
        super(position);
        this.variable = variable;
        this.value = value;
    }

    public Variable getVariable() {
        return variable;
    }

    public Expression getValue() {
        return value;
    }
}
