package Utilities.ProgramTree;

import Utilities.ProgramTree.Variables.Variable;

import java.util.ArrayList;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Parameters implements INode {
    private final ArrayList<Variable> parameters;

    public Parameters() {
        parameters = new ArrayList<>();
    }

    public void add(Variable variable) {
        parameters.add(variable);
    }

    public ArrayList<Variable> getParameters() {
        return parameters;
    }

    public int getParametersNumber() {
        return parameters.size();
    }
}
