package Scanner;

import Source.ISource;
import Source.Position;
import Utilities.Constants;
import Utilities.ScannerKeywords;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Scanner {
    private Token token;
    private final ISource source;
    // tempType - variable used to simplify function calls. It is used to create new Token.
    private Token.Type tempType;
    // position - same as tempType but for position, not type of token.
    private Position position;
    // undefString - variable used to store current string. Used for filling value member of Token.
    private StringBuilder undefString;

    public Scanner(ISource source) {
        this.source = source;
        this.token = new Token(Token.Type.Undefined, new Position());
    }

    public void next() throws Exception {
        ignoreWhiteSpaces();

        position = (Position) source.getPosition().clone();
        undefString = null;

        if (tryEof()) {
            return;
        } else if (trySingleSymbols()) {
            return;
        } else if (tryDoubleSymbol()) {
            return;
        } else if (tryMultiSymbols()) {
            return;
        } else if (tryString()) {
            return;
        } else if (tryHexOrPlanet()) {
            return;
        } else if (tryIdentifierOrKeywordOrLiteral()) {
            return;
        } else if (tryNumber()) {
            return;
        }
        // If undefString wasn't initialized, we passed all tries so it's unknown char from source.
        // If undefString is initialized then it's unknown string created by one of tries.
        String unknownSymbol = undefString != null ? undefString.toString() : "" + (char) source.peek();
        throw new Exception("Undefined symbol: " + unknownSymbol + " at " + position + ".");
    }

    public Token get() throws Exception {
        Token tempToken = this.token;
        this.next();
        return tempToken;
    }

    public Token peek(){
        return this.token;
    }

    private boolean tryEof() throws Exception {
        if (source.peek() == Constants.Source.EOF) {
            token = new Token(Token.Type.EOF, position);
            source.next();
            return true;
        }
        return false;
    }

    private boolean trySingleSymbols() throws Exception {
        // Single symbols = symbols that are single characters.
        tempType = ScannerKeywords.singleToType.get("" + (char) source.peek());
        if (tempType != null) {
            token = new Token(tempType, position);
            source.next();
            return true;
        }
        return false;
    }

    private boolean tryDoubleSymbol() throws Exception {
        // Double symbols - symbols that have two same characters like "||".
        char tempChar = (char) source.peek();
        if (ScannerKeywords.doubleSymbols.get(tempChar) != null) {
            source.next();
            if (tempChar == (char) source.peek()) {
                tempType = ScannerKeywords.symbolToType.get("" + tempChar + tempChar);
            } else {
                throw new Exception("Double symbol error at " + position + ".");
            }
        } else {
            return false;
        }
        token = new Token(tempType, position);
        source.next();
        return true;
    }

    private boolean tryMultiSymbols() throws Exception {
        // Multi symbols - symbols that can have more than one characters that are not all the same like "<=".
        StringBuilder symbols = new StringBuilder();
        symbols.append((char) source.peek());
        if (ScannerKeywords.multiSymbols.get(symbols.charAt(0)) != null) {
            source.next();
            if (ScannerKeywords.multiSymbols.get(symbols.charAt(0)).contains((char) source.peek())) {
                symbols.append((char) source.get());
                tempType = ScannerKeywords.symbolToType.get(symbols.toString());
            } else {
                tempType = ScannerKeywords.firstFromMultiSymbol.get(symbols.charAt(0));
                if (tempType != null) {
                    token = new Token(tempType, position);
                    return true;
                }
            }
        } else {
            return false;
        }
        token = new Token(tempType, position);
        return true;
    }

    private boolean tryString() throws Exception {
        if ((char) source.peek() == '\"') {
            source.next();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\"');
            while (isCharacter((char) source.peek()) && stringBuilder.length() < Constants.Token.MAX_STRING_LEN) {
                stringBuilder.append((char) source.get());
            }
            if ((char) source.peek() == '\"') {
                stringBuilder.append((char) source.get());
                token = new Token(Token.Type.StringLiteral, position, stringBuilder.toString());
                return true;
            } else {
                throw new Exception("String not valid at " + position + ".");
            }
        }
        return false;
    }

    private boolean isCharacter(char c) {
        return isDigit(c) || isLetter(c) || isSpecialCharacter(c);
    }

    private boolean isSpecialCharacter(char c) {
        return Constants.Token.SPECIAL_CHARS.contains(c);
    }

    private boolean tryHexOrPlanet() throws Exception {
        if (isHexOrPlanet((char) source.peek())) {
            undefString = new StringBuilder();
            undefString.append((char) source.get());
            if (isZeroDigit((char) source.peek())) {
                undefString.append((char) source.get());
            } else if (isNonZeroDigit((char) source.peek())) {
                undefString.append((char) source.get());
                while (isDigit((char) source.peek()) && checkNumber(undefString.substring(1) + (char) source.peek())) {
                    undefString.append((char) source.get());
                }
            } else {
                return false;
            }
            if (!isIdentifierCharacter((char) source.peek())) {
                tempType = ScannerKeywords.hexOrPlanetToType.get(undefString.substring(0, 1));
                token = new Token(tempType, position, undefString.toString());
                return true;
            }
        }
        return false;
    }

    private boolean checkNumber(String string) throws NumberFormatException {
        return Integer.parseInt(string) < Constants.Token.MAX_NUMBER_VAL;
    }

    private boolean isHexOrPlanet(char c) {
        return c == 'h' || c == 'p';
    }

    private boolean tryIdentifierOrKeywordOrLiteral() throws Exception {
        if (undefString == null) {
            if (isDigit((char) source.peek())) {
                return false;
            }
            undefString = new StringBuilder();
        } else if (isDigit(undefString.charAt(undefString.length() - 1))) {
            // It wasn't hex/planet because after ("h/p", number) appeared identifierCharacter - we need to include him.
            undefString.append((char) source.get());
        }
        while (undefString.length() < Constants.Token.MAX_IDENTIFIER_LEN) {
            if (isIdentifierCharacter((char) source.peek())) {
                undefString.append((char) source.get());
            } else {
                if (checkKeywordLiteralIdentifier()) {
                    token = new Token(tempType, position, undefString.toString());
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (undefString.length() == Constants.Token.MAX_IDENTIFIER_LEN) {
            throw new Exception("Too long identifier at " + position + ".");
        }
        return false;
    }

    private boolean checkKeywordLiteralIdentifier() {
        return checkIfKeyword() || checkIfLiteral() || checkIfIdentifier();
    }

    private boolean checkIfKeyword() {
        if (undefString != null) {
            tempType = ScannerKeywords.keywordToType.get(undefString.toString());
            return tempType != null;
        }
        return false;
    }

    private boolean checkIfLiteral() {
        if (undefString != null) {
            tempType = ScannerKeywords.literalToType.get(undefString.toString());
            return tempType != null;
        }
        return false;
    }

    private boolean checkIfIdentifier() {
        if (undefString != null && undefString.length() > 0) {
            if (isLetter(undefString.charAt(0)) || (isUnderscore(undefString.charAt(0)) && undefString.length() > 1)) {
                tempType = Token.Type.Identifier;
                return true;
            }
        }
        return false;
    }

    private boolean isIdentifierCharacter(char c) {
        return isLetter(c) || isDigit(c) || isUnderscore(c);
    }

    private boolean isUnderscore(char c) {
        return c == '_';
    }

    private boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean tryNumber() throws Exception {
        // Either only zero or non zero digit with different digits next.
        if (isZeroDigit((char) source.peek())) {
            token = new Token(Token.Type.NumberLiteral, position, "" + (char) source.get());
            if (isDigit((char) source.peek())) {
                throw new Exception("Wrong number at " + position);
            }
            return true;
        } else if (isNonZeroDigit((char) source.peek())) {
            undefString = new StringBuilder();
            undefString.append((char) source.get());
            char current;
            while (isDigit(current = (char) source.peek()) && checkNumber(undefString.toString() + current)) {
                undefString.append((char) source.get());
            }
            if (isIdentifierCharacter((char) source.peek())) {
                tempType = Token.Type.Undefined;
                return false;
            }
            token = new Token(Token.Type.NumberLiteral, position, undefString.toString());
            return true;
        }
        return false;
    }

    private boolean isNonZeroDigit(char c) {
        return c > '0' && c <= '9';
    }

    private boolean isZeroDigit(char c) {
        return c == '0';
    }

    private void ignoreWhiteSpaces() throws Exception {
        while (Character.isWhitespace(source.peek())) {
            source.next();
        }
    }
}
