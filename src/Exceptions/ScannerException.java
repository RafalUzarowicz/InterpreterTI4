package Exceptions;

import Utilities.Position;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class ScannerException extends InterpretingException {
    public ScannerException(Position position, String message) {
        super(position, "Lexical error: " + message);
    }
}
