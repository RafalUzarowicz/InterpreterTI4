package Scanner;

import Source.Position;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Token {
    public enum Type {
        Identifier,             // identifier = {underscore}, letter, {letter}, {digit}
        Type,                   // "int", "string", "bool" etc.
        VarType,                // "var"

        Player,                 // "player"
        Planet,                 // "planet"
        Hex,                    // "hex"
        Move,                   // "move"
        Add,                    // "add"
        Remove,                 // "remove"
        From,                   // "from"
        To,                     // "to"
        Has,                    // "has"
        At,                     // "at"
        Activated,              // "activated"
        Activate,               // "activate"
        Deactivate,             // "activate"

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
    /**
     * value - holds literal values but also things like specific variable type. It is different based on token type.
     */
    private final String value;
    private final Position position;

    public Token(Type type, Position position) {
        this(type, position, null);
    }

    public Token(Type type, Position position, String value) {
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

    public String toString() {
        return "TOKEN( Type: " + type + ", Position: " + position + ((value != null) ? (", Value: " + value) : ("")) + " )";
    }
}
