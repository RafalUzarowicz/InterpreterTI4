package Interpreter.Environment;

import Utilities.InterpreterUtils;
import Utilities.ProgramTree.Variables.Variable;

import java.util.LinkedHashMap;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class BlockContext {
    private final LinkedHashMap<String, Variable> variables;

    public BlockContext() {
        variables = new LinkedHashMap<>();
    }

    public void setVariable(String name, Variable variable) {
        variables.put(name, InterpreterUtils.variableCopy(variable));
    }

    public boolean contains(String name) {
        return variables.get(name) != null;
    }

    public Variable getVariable(String name) {
        return variables.get(name);
    }

    public int getSize() {
        return variables.size();
    }
}
