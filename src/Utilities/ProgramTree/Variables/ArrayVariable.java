package Utilities.ProgramTree.Variables;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Literals.Literal;
import Utilities.ProgramTree.Value.Value;

import java.util.ArrayList;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class ArrayVariable extends Variable implements INode {
    private final ArrayList<Literal> values;
    private final Variable variable;


    public ArrayVariable(Variable variable, int size) {
        super(variable.getName());
        this.variable = variable;
        values = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            values.add(null);
        }
    }

    public void setValue(int index, Literal value) {
        values.set(index, value);
    }

    public Value getValue(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }

    public Variable getVariable() {
        return variable;
    }
}
