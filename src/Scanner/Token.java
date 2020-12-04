package Scanner;

import Source.Position;

public class Token {
    public enum Type{
        Identifier,             // identifier = {underscore}, letter, {letter}, {digit}
        Type,                   // "int", "string", "bool" etc.
        VarType,                // "var"

        Player,                 // "player"
        Planet,                 // "planet"
        Hex,                    // "hex"
        UnitsAction,            // "move", "add", "remove"
        From,                   // "from"
        To,                     // "to"
        Has,                    // "has"
        At,                     // "at"
        Activated,              // "activated"
        Activation,             // "activate", "deactivate"

        NumberLiteral,          // number = nonZeroNumber | "0"; nonZeroNumber = nonZeroDigit, {digit};
        StringLiteral,          // string = """, {character}, """
        BoolLiteral,            // bool = "true" | "false"
        UnitLiteral,            // "Fighter", "Destroyer", "Carrier" etc.
        ColorLiteral,           // "Red", "Yellow", "Green" etc.
        HexLiteral,             // hex = "h", "0".."50"
        PlanetLiteral,          // planet = "p", "0".."58"

        ParenthesisLeft,        // "("
        ParenthesisRight,       // ")"
        BracketsLeft,           // "["
        BracketsRight,          // "]"
        BracesLeft,             // "{"
        BracesRight,            // "}"

        Comma,                  // ","
        Colon,                  // ":"
        Semicolon,              // ";"
        Minus,                  // "-"
        Plus,                   // "+"
        Multiply,               // "*"
        Divide,                 // "/"

        And,                    // "&&"
        Or,                     // "||"

        Greater,                // ">"
        Less,                   // "<"
        Not,                    // "!"
        Equals,                 // "="

        GreaterEqual,           // ">="
        LessEqual,              // "<="
        Equal,                  // "=="
        NotEqual,               // "!="

        Foreach,                // "foreach"
        Continue,               // "continue"
        Break,                  // "break"
        Return,                 // "return"
        Print,                  // "print"
        If,                     // "if"
        Else,                   // "else"

        EOF,                    // End of file
        Undefined               // Unknown token
    }

    private final Type type;
    private final String value;
    private final Position position;

    public Token(Type type, Position position){
        this(type, position, null);
    }

    public Token(Type type, Position position, String value){
        this.position = position;
        this.value = value;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Position getPosition() {
        return position;
    }

    public String toString()
    {
        return "( Type: "+type+"; Position: "+position+((value!=null)?("; Value: "+value):(""))+ " )";
    }
}
