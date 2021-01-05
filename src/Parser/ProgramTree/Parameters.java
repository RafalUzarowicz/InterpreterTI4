package Parser.ProgramTree;

import Parser.Parser;

import java.util.ArrayList;

public class Parameters {
    private ArrayList<Variable> parameters;

    public Parameters() {
        parameters = new ArrayList<>();
    }

    public void add(Variable variable) {
        parameters.add(variable);
    }
}
