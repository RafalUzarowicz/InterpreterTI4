package Exceptions;

import Scanner.Token;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class ParserException extends Exception{
    public ParserException(Token token, Token.Type type){
        super(token.getPosition() + " Expected: "+type.toString() + " - got: " + token.toString());
    }
    public ParserException(Token token, String message){
        super(token.getPosition() + " " + message );
    }
    public ParserException(Token token){
        super(token.getPosition() + " Token not expected: " + token.getType().toString() );
    }
}