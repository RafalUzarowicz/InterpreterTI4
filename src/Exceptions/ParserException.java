package Exceptions;

import Scanner.Token;

public class ParserException extends Exception{
    public ParserException(Token token, Token.Type type){
        super(token.getPosition() + " Expected: "+type.toString() + " - got: " + token.toString());
    }
}
