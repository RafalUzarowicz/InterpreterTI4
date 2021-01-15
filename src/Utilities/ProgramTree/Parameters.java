package Utilities.ProgramTree;

import Utilities.ProgramTree.Variables.Variable;

import java.util.ArrayList;

public class Parameters {
    private ArrayList<Variable> parameters;

    public Parameters() {
        parameters = new ArrayList<>();
    }

    public void add(Variable variable) {
        parameters.add(variable);
    }

    public ArrayList<Variable> getParameters() {
        return parameters;
    }
}
