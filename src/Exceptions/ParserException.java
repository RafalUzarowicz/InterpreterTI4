package Exceptions;

import Utilities.Token;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class ParserException extends InterpretingException {
    public ParserException(Token token, Token.Type type) {
        super(token.getPosition(), "Parsing error: Expected: " + type.toString() + " got: " + token.getType());
    }

    public ParserException(Token token, String message) {
        super(token.getPosition(), "Parsing error: " + message);
    }
}
