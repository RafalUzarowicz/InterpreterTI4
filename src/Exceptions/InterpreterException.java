package Exceptions;

import Utilities.Position;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class InterpreterException extends InterpretingException {
    public InterpreterException(String message) {
        super("Interpreting error: " + message);
    }

    public InterpreterException(int line, String message) {
        super(new Position(line, 0), "Interpreting error: " + message);
    }
}
