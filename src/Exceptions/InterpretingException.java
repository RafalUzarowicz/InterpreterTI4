package Exceptions;

import Utilities.Position;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class InterpretingException extends Exception {
    public InterpretingException(Position position, String message){
        super("At line " + position.getLine() +" : "+message);
    }

    public InterpretingException(String message){
        super(message);
    }
}
