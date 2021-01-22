package Interpreter.Environment;

import Exceptions.InterpreterException;
import Utilities.ProgramTree.Variables.Variable;

import java.util.LinkedList;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class CallContext {
    private final LinkedList<BlockContext> blockContexts;

    public CallContext() {
        blockContexts = new LinkedList<>();
    }

    public void pushBlockContext() {
        blockContexts.push(new BlockContext());
    }

    public BlockContext peekBlockContext() {
        return blockContexts.peek();
    }

    public void popBlockContext() {
        blockContexts.pop();
    }

    public void setVariableValue(int line, String name, String value) throws InterpreterException {
        Variable variable;
        for (var context : blockContexts) {
            variable = context.getVariable(name);
            if (variable != null) {
                variable.setValue(value);
                return;
            }
        }
        throw new InterpreterException(line, "Unknown variable identifier: " + name);
    }

    public Variable getVariable(int line, String name) throws InterpreterException {
        Variable variable;
        for (var context : blockContexts) {
            variable = context.getVariable(name);
            if (variable != null) {
                return variable;
            }
        }
        throw new InterpreterException(line, "Unknown variable identifier: " + name);
    }
}
