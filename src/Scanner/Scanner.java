package Scanner;

import Source.ISource;
import Source.Position;

public class Scanner {
    private Token token;
    private ISource source;
    private Token.Type tempType;

    public Scanner(ISource source){
        this.source = source;
        this.token = new Token(Token.Type.Undefined, new Position());
    }

    public void next(){
        ignoreWhiteSpaces();
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
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        try {
            if(tryIdentifierOrKeywordOrLiteral()){
                return;
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if(tryNumber()){
            return;
        }


        this.token = new Token(Token.Type.Undefined, source.getPosition());
    }

    public Token get(){
        return token;
    }

    private boolean tryEof(){
        if(source.peek() == -1){
            token = new Token(Token.Type.EOF, source.getPosition());
            source.next();
            return true;
        }
        return false;
    }

    private boolean trySingleSymbols(){
        tempType = Keywords.singleToType.get(""+(char)source.peek());
        if(tempType != null){
            token = new Token(tempType, source.getPosition());
            return true;
        }
        return false;
    }

    private boolean tryDoubleSymbol() throws Exception {
        char tempChar = (char) source.peek();
        Position position = source.getPosition();
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
        Position position = source.getPosition();
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
        Position position = source.getPosition();
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
        return c == '_' || c =='.'|| c == ',' || c == '-' || c ==' ' || c== '/' || c =='\\';
    }

    private boolean tryIdentifierOrKeywordOrLiteral() throws Exception {
        Position position = source.getPosition();
        if((char)source.peek() == '_'){
            try{
                return tryIdentifier(new StringBuilder());
            } catch (Exception exception) {
                // TODO: lepsza obsluga wyjatkow
                throw new Exception(exception.getMessage());
            }
        }
        if(!isDigit((char)source.peek())){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((char)source.get());

            if(stringBuilder.charAt(0) == 'h' || stringBuilder.charAt(0) =='p'){
                if(tryHexOrPlanet(stringBuilder, position)){
                    return true;
                }
            }
            int currLen = stringBuilder.length();
            do{
                if(tryKeyword(stringBuilder, position)){
                    return true;
                } else if(tryLiteral(stringBuilder, position)){
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

            try{
                return tryIdentifier(stringBuilder);
            } catch (Exception exception) {
                // TODO: lepsza obsluga wyjatkow
                throw new Exception(exception.getMessage());
            }
        }
        return false;
    }

    private boolean isDigitOrLetter(char c){
        return isDigit(c)||isLetter(c);
    }

    private boolean tryKeyword(StringBuilder stringBuilder, Position position){
        tempType = Keywords.keywordToType.get(stringBuilder.toString());
        if(tempType != null){
            token = new Token(tempType, position);
            return true;
        }
        return false;
    }

    private boolean tryLiteral(StringBuilder stringBuilder, Position position){
        tempType = Keywords.literalToType.get(stringBuilder.toString());
        if(tempType != null){
            token = new Token(tempType, position, stringBuilder.toString());
            return true;
        }
        return false;
    }

    private boolean tryHexOrPlanet(StringBuilder stringBuilder, Position position){
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
        Position position = source.getPosition();
        boolean isIdentifier = false;
        while((char)source.peek() == '_'){
            stringBuilder.append((char)source.get());
        }
        if(isLetter((char)source.peek())){
            stringBuilder.append((char)source.get());
            isIdentifier = true;
        }else{
            // TODO: lepsza obsluga wyjatkow
            throw new Exception("Wrong identifier at "+position+".");
        }
        while(isLetter((char)source.peek())){
            stringBuilder.append((char)source.get());
        }
        while(isDigit((char)source.peek())){
            stringBuilder.append((char)source.get());
        }

        token = new Token(Token.Type.Identifier, position, stringBuilder.toString());
        source.next();
        return isIdentifier;
    }

    private boolean isLetter(char c){
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    private boolean tryNumber(){
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
