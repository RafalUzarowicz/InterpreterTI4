package Scanner;

import Source.ISource;
import Source.Position;

public class Scanner {
    private Token token;
    private final ISource source;
    private Token.Type tempType;
    private Position position;

    public Scanner(ISource source){
        this.source = source;
        this.token = new Token(Token.Type.Undefined, new Position());
    }

    public void next(){
        ignoreWhiteSpaces();
        position = (Position) source.getPosition().clone();
        if(tryEof()){
            return;
        } else if(trySingleSymbols()){
            return;
        }
        try {
            if(tryDoubleSymbol()){
                return;
            }
        } catch (Exception exception) {
            // TODO: lepsza obsluga wyjatkow
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if(tryMultiSymbols()){
            return;
        }
        try {
            if(tryString()){
                return;
            }
        } catch (Exception exception) {
            // TODO: lepsza obsluga wyjatkow
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        try {
            if(tryIdentifierOrKeywordOrLiteral()){
                return;
            }
        } catch (Exception exception) {
            // TODO: lepsza obsluga wyjatkow
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if(tryNumber()){
            return;
        }


        this.token = new Token(Token.Type.Undefined, position);
    }

    public Token get(){
        return token;
    }

    private boolean tryEof(){
        if(source.peek() == -1){
            token = new Token(Token.Type.EOF, (Position) source.getPosition().clone());
            source.next();
            return true;
        }
        return false;
    }

    private boolean trySingleSymbols(){
        tempType = Keywords.singleToType.get(""+(char)source.peek());
        if(tempType != null){
            token = new Token(tempType, (Position) source.getPosition().clone());
            source.next();
            return true;
        }
        return false;
    }

    private boolean tryDoubleSymbol() throws Exception {
        char tempChar = (char) source.peek();
        if(Keywords.doubleSymbols.get(tempChar) != null){
            source.next();
            if(tempChar == (char) source.peek()){
                switch(tempChar){
                    case '&':
                        tempType = Token.Type.And;
                        break;
                    case '|':
                        tempType = Token.Type.Or;
                        break;
                    default:
                        break;
                }
            }else{
                // TODO: lepsza obsluga wyjatkow
                throw new Exception("Double symbol error at "+position+".");
            }
        }else{
            return false;
        }
        token = new Token(tempType, position);
        source.next();
        return true;
    }

    private boolean tryMultiSymbols(){
        StringBuilder symbols = new StringBuilder();
        symbols.append((char) source.peek());
        if(Keywords.multiSymbols.get(symbols.charAt(0)) != null){
            source.next();
            symbols.append((char) source.peek());
            if(Keywords.multiSymbols.get(symbols.charAt(0)).contains(symbols.charAt(1))){
                tempType = Keywords.symbolToType.get(symbols.toString());
                source.next();
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
        token = new Token(tempType, position);
        return true;
    }

    private boolean tryString() throws Exception {
        if((char)source.peek() == '\"'){
            source.next();
            StringBuilder stringBuilder = new StringBuilder();
            while(isCharacter((char)source.peek())){
                stringBuilder.append((char)source.get());
            }
            if( (char)source.peek() == '\"'){
                token = new Token(Token.Type.StringLiteral, position, stringBuilder.toString());
                source.next();
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
        if((char)source.peek() == '_'){
            try{
                return tryIdentifier(null);
            } catch (Exception exception) {
                // TODO: lepsza obsluga wyjatkow
                throw new Exception(exception.getMessage());
            }
        }
        if(!isDigit((char)source.peek())){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((char)source.get());

            if(stringBuilder.charAt(0) == 'h' || stringBuilder.charAt(0) =='p'){
                if(tryHexOrPlanet(stringBuilder)){
                    return true;
                }
            }
            int currLen = stringBuilder.length();
            do{
                if((tryKeyword(stringBuilder) || tryLiteral(stringBuilder)) && !isLetter((char)source.peek()) && !isDigit((char)source.peek()) ){
                    token = new Token(tempType, position, stringBuilder.toString());
                    return true;
                }
                if(isLetter((char)source.peek())){
                    stringBuilder.append((char)source.get());
                }else{
                    break;
                }
                ++currLen;
                // TODO: zamiast iden len to najwieksza dlugosc slowa z keywords
            }while(currLen < Constants.Token.MAX_IDENTIFIER_LEN);

            if(currLen == Constants.Token.MAX_IDENTIFIER_LEN){
                throw new Exception("Too long identifier at "+position+".");
            }
            try{
                return tryIdentifier(stringBuilder);
            } catch (Exception exception) {
                // TODO: lepsza obsluga wyjatkow
                throw new Exception(exception.getMessage());
            }
        }
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
            if( checkPlanetOrHexIndex(value, stringBuilder.charAt(0)) ){
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
        if (stringBuilder == null || stringBuilder.length() <= 0) {
            stringBuilder = new StringBuilder();
            while((char)source.peek() == '_'){
                stringBuilder.append((char)source.get());
            }
            if(isLetter((char)source.peek())){
                stringBuilder.append((char)source.get());
            }else{
                return false;
            }
        }
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

    private boolean isLetter(char c){
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    private boolean tryNumber(){
        tempType = Token.Type.NumberLiteral;
        if((char)source.peek() == '0'){
            token = new Token(tempType, position, "" + (char) source.get());
            return true;
        }else if(isNonZeroDigit((char)source.peek())){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((char)source.get());
            char current;
            try {
                while (isDigit(current = (char) source.peek()) && Integer.parseInt(stringBuilder.toString() + current) < Constants.Token.MAX_NUMBER_VAL) {
                    stringBuilder.append((char)source.get());
                }
            } catch (NumberFormatException exception){
                // TODO: lepsza obsluga wyjatkow
                System.out.println(exception.getMessage());
                exception.printStackTrace();
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
