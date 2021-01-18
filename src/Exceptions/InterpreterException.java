package Exceptions;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class InterpreterException extends Exception{
    public InterpreterException(int line, String message){

        super("<Line: "+line+"> "+message);
    }
    public InterpreterException(String message){

        super(message);
    }
}
