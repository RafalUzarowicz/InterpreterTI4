package Scanner;

import Source.ISource;
import Source.Position;
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

    public Scanner(ISource source){
        this.source = source;
        this.token = new Token(Token.Type.Undefined, new Position());
    }

    public void next() throws Exception {
        ignoreWhiteSpaces();

        position = (Position) source.getPosition().clone();
        undefString = null;

        if(tryEof()){
            return;
        }else if(trySingleSymbols()){
            return;
        }else if(tryDoubleSymbol()){
            return;
        }else if(tryMultiSymbols()){
            return;
        }else if(tryString()){
            return;
        }else if(tryIdentifierOrKeywordOrLiteral()){
            return;
        }else if(tryNumber()){
            return;
        }
        // TODO: usun to
//        this.token = new Token(Token.Type.Undefined, position, (undefString != null?undefString.toString():""+(char)source.peek()));
        throw new Exception("Undefined symbol: "+(undefString != null?undefString.toString():""+(char)source.peek())+" at "+position+".");
    }

    public Token get(){
        return token;
    }

    private boolean tryEof(){
        if(source.peek() == -1){
            token = new Token(Token.Type.EOF, position, "eof");
            source.next();
            return true;
        }
        return false;
    }

    private boolean trySingleSymbols(){
        // Single symbols = symbols that are single characters.
        tempType = Keywords.singleToType.get(""+(char)source.peek());
        if(tempType != null){
            token = new Token(tempType, position, ""+(char)source.get());
            return true;
        }
        return false;
    }

    private boolean tryDoubleSymbol() throws Exception {
        // Double symbols - symbols that have two same characters like "||".
        char tempChar = (char) source.peek();
        if(Keywords.doubleSymbols.get(tempChar) != null){
            source.next();
            if(tempChar == (char) source.peek()){
                tempType = Keywords.symbolToType.get(""+tempChar+tempChar);
            }else{
                // TODO: lepsza obsluga wyjatkow
                throw new Exception("Double symbol error at "+position+".");
            }
        }else{
            return false;
        }
        token = new Token(tempType, position, ""+tempChar+tempChar);
        source.next();
        return true;
    }

    private boolean tryMultiSymbols(){
        // Multi symbols - symbols that can have more than one characters that are not all the same like "<=".
        StringBuilder symbols = new StringBuilder();
        symbols.append((char) source.peek());
        if(Keywords.multiSymbols.get(symbols.charAt(0)) != null){
            source.next();
            if(Keywords.multiSymbols.get(symbols.charAt(0)).contains((char) source.peek())){
                symbols.append((char) source.get());
                tempType = Keywords.symbolToType.get(symbols.toString());
            }else{
                switch(symbols.charAt(0)){
                    case '>':
                        tempType = Token.Type.Greater;
                        break;
                    case '<':
                        tempType = Token.Type.Less;
                        break;
                    case '!':
                        tempType = Token.Type.Not;
                        break;
                    case '=':
                        tempType = Token.Type.Equals;
                        break;
                    default:
                        break;
                }
            }
        }else{
            return false;
        }
        token = new Token(tempType, position, symbols.toString());
        return true;
    }

    private boolean tryString() throws Exception {
        if((char)source.peek() == '\"'){
            source.next();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\"');
            while(isCharacter((char)source.peek()) && stringBuilder.length()<Constants.Token.MAX_STRING_LEN){
                stringBuilder.append((char)source.get());
            }
            if( (char)source.peek() == '\"'){
                stringBuilder.append((char)source.get());
                token = new Token(Token.Type.StringLiteral, position, stringBuilder.toString());
                return true;
            }else{
                // TODO: lepsza obsluga wyjatkow
                throw new Exception("String not valid at "+position+".");
            }
        }
        return false;
    }

    private boolean isCharacter(char c){
        return isDigit(c) || isLetter(c) || isSpecialCharacter(c);
    }

    private boolean isSpecialCharacter(char c){
        // TODO: escape characters
        return Constants.Token.SPECIAL_CHARS.contains(c);
    }

    private boolean tryIdentifierOrKeywordOrLiteral() throws Exception {
        // Only identifiers can start with '_'.
        if((char)source.peek() == '_'){
            try{
                return tryIdentifier(null);
            } catch (Exception exception) {
                // TODO: lepsza obsluga wyjatkow
                throw new Exception(exception.getMessage());
            }
        }
        // It's not '_' and not digit -> it can be keyword, literal or identifier.
        if(!isDigit((char)source.peek())){
            undefString = new StringBuilder();
            // stringBuilder - we will hold current characters that we read in it.
            StringBuilder stringBuilder = undefString;
            stringBuilder.append((char)source.get());
            // If it starts with h and p it can be hex/planet literal.
            if(stringBuilder.charAt(0) == 'h' || stringBuilder.charAt(0) =='p'){
                if(tryHexOrPlanet(stringBuilder)){
                    return true;
                }
            }
            // It doesnt start with h/p or it starts with h/p but it doesn't fulfills proper requirements.
            // If last character is not letter we move on.
            if(isLetter(stringBuilder.charAt(stringBuilder.length()-1))) {
                // currLen - just to check if we didn't pass limits.
                int currLen = stringBuilder.length();
                // Add new letter and then check if current string is keyword/literal.
                do {
                    if ((tryKeyword(stringBuilder) || tryLiteral(stringBuilder)) && !isLetter((char) source.peek()) && !isDigit((char) source.peek())) {
                        token = new Token(tempType, position, stringBuilder.toString());
                        return true;
                    }
                    if (isLetter((char) source.peek())) {
                        // Only letters can be used for keywords/literals.
                        stringBuilder.append((char) source.get());
                    } else {
                        break;
                    }
                    ++currLen;
                    // TODO: zamiast iden len to najwieksza dlugosc slowa z keywords
                } while (currLen < Constants.Token.MAX_IDENTIFIER_LEN);

                if (currLen == Constants.Token.MAX_IDENTIFIER_LEN) {
                    // TODO: lepsza obsluga wyjatkow
                    throw new Exception("Too long identifier at " + position + ".");
                }
            }

            // We try to create identifier with what we have.
            try{
                return tryIdentifier(stringBuilder);
            } catch (Exception exception) {
                // TODO: lepsza obsluga wyjatkow
                throw new Exception(exception.getMessage());
            }
        }
        // It is digit so we move on.
        return false;
    }

    private boolean tryKeyword(StringBuilder stringBuilder){
        if( stringBuilder != null ){
            tempType = Keywords.keywordToType.get(stringBuilder.toString());
            return tempType != null;
        }
        return false;
    }

    private boolean tryLiteral(StringBuilder stringBuilder){
        if( stringBuilder != null ) {
            tempType = Keywords.literalToType.get(stringBuilder.toString());
            return tempType != null;
        }
        return false;
    }

    private boolean tryHexOrPlanet(StringBuilder stringBuilder){
        if(isDigit((char)source.peek())){
            stringBuilder.append((char)source.get());
            if(isDigit((char)source.peek())){
                stringBuilder.append((char)source.get());
            }
            int value = Integer.parseInt(stringBuilder.substring(1));
            if( !(isDigit((char)source.peek())||isLetter((char)source.peek())) && checkPlanetOrHexIndex(value, stringBuilder.charAt(0)) ){
                tempType = Keywords.hexOrPlanetToType.get(stringBuilder.substring(0, 1));
                token = new Token(tempType, position, stringBuilder.toString());
                return true;
            }
        }
        return false;
    }

    private boolean checkPlanetOrHexIndex(int index, char planetOrHex){
        return (planetOrHex == 'p' && index>=0 && index<Constants.Board.PLANET_NUMBER) || (planetOrHex == 'h' && index>=0 && index<Constants.Board.HEX_NUMBER);
    }

    private boolean tryIdentifier(StringBuilder stringBuilder) throws Exception {
        // If we didn't collected something already we need to start from scratch.
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
            undefString = stringBuilder;
            while((char)source.peek() == '_'){
                stringBuilder.append((char)source.get());
                if(stringBuilder.length()>=Constants.Token.MAX_IDENTIFIER_LEN-1){
                    throw new Exception("Identifier name too long at "+position+".");
                }
            }
            // We must have at least one letter.
            if(isLetter((char)source.peek())){
                stringBuilder.append((char)source.get());
            }else{
                return false;
            }
        }
        if( stringBuilder.length() > 0 && isDigit(stringBuilder.charAt(stringBuilder.length()-1)) && (isLetter((char)source.peek())||(char)source.peek()=='_')){
            throw new Exception("Wrong identifier name: "+stringBuilder.toString()+(char)source.peek()+" at "+position+".");
        }
        // If last character is letter we can continue creating identifier.
        if( stringBuilder.length() > 0 && isLetter(stringBuilder.charAt(stringBuilder.length()-1)) ){
            while(isLetter((char)source.peek())){
                stringBuilder.append((char)source.get());
                if(stringBuilder.length()>=Constants.Token.MAX_IDENTIFIER_LEN){
                    throw new Exception("Identifier name too long at "+position+".");
                }
            }
            while(isDigit((char)source.peek())){
                stringBuilder.append((char)source.get());
                if(stringBuilder.length()>=Constants.Token.MAX_IDENTIFIER_LEN){
                    throw new Exception("Identifier name too long at "+position+".");
                }
            }
            token = new Token(Token.Type.Identifier, position, stringBuilder.toString());
            return true;
        }
        // If lst character is digit we can continue creating identifier.
        if( stringBuilder.length() > 0 && isDigit(stringBuilder.charAt(stringBuilder.length()-1)) ) {
            while(isDigit((char)source.peek())){
                stringBuilder.append((char)source.get());
                if(stringBuilder.length()>=Constants.Token.MAX_IDENTIFIER_LEN){
                    throw new Exception("Identifier name too long at "+position+".");
                }
            }
            token = new Token(Token.Type.Identifier, position, stringBuilder.toString());
            return true;
        }
        return false;
    }

    private boolean isLetter(char c){
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    private boolean tryNumber() throws Exception {
        tempType = Token.Type.NumberLiteral;
        // Either only zero or non zero digit with different digits next.
        if((char)source.peek() == '0'){
            token = new Token(tempType, position, "" + (char) source.get());
            if(isDigit((char)source.peek())){
                throw new Exception("Wrong number at "+position);
            }
            return true;
        }else if(isNonZeroDigit((char)source.peek())){
            undefString = new StringBuilder();
            // stringBuilder - we will hold current characters that we read in it.
            StringBuilder stringBuilder = undefString;
            stringBuilder.append((char)source.get());
            char current;
            try {
                while (isDigit(current = (char) source.peek()) && Integer.parseInt(stringBuilder.toString() + current) < Constants.Token.MAX_NUMBER_VAL) {
                    stringBuilder.append((char)source.get());
                }
            } catch (NumberFormatException exception){
                // TODO: lepsza obsluga wyjatkow
                throw new Exception("Number too large at "+position);
            }
            token = new Token(tempType, position, stringBuilder.toString());
            return true;
        }
        return false;
    }

    private boolean isNonZeroDigit(char c){
        return c > '0' && c <= '9';
    }

    private void ignoreWhiteSpaces(){
        while(Character.isWhitespace(source.peek())){
            source.next();
        }
    }
}
