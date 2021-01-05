package Utilities;

import java.util.*;
import Scanner.Token;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class that associate symbols and words with proper token types.
 */
public class ScannerKeywords {
    public final static HashMap<Character, ArrayList<Character>> doubleSymbols = new HashMap<>() {{
        put('&', new ArrayList<>(Collections.singletonList('&')));
        put('|', new ArrayList<>(Collections.singletonList('|')));
    }};
    public final static HashMap<Character, ArrayList<Character>> multiSymbols = new HashMap<>() {{
        put('>', new ArrayList<>(Collections.singletonList('=')));
        put('<', new ArrayList<>(Collections.singletonList('=')));
        put('!', new ArrayList<>(Collections.singletonList('=')));
        put('=', new ArrayList<>(Collections.singletonList('=')));
    }};
    public final static HashMap<Character, Token.Type> firstFromMultiSymbol = new HashMap<>() {{
        put('>', Token.Type.Greater);
        put('<', Token.Type.Less);
        put('!', Token.Type.Not);
        put('=', Token.Type.Equals);
    }};
    public final static HashMap<String, Token.Type> symbolToType = new HashMap<>() {{
        put(">=", Token.Type.GreaterEqual);
        put("<=", Token.Type.LessEqual);
        put("==", Token.Type.Equal);
        put("!=", Token.Type.NotEqual);
        put("&&", Token.Type.And);
        put("||", Token.Type.Or);
    }};

    public final static HashMap<String, Token.Type> hexOrPlanetToType = new HashMap<>() {{
        put("h", Token.Type.HexLiteral);
        put("p", Token.Type.PlanetLiteral);
    }};

    public final static HashMap<String, Token.Type> singleToType = new HashMap<>() {{
        put("(", Token.Type.ParenthesisLeft);
        put(")", Token.Type.ParenthesisRight);
        put("[", Token.Type.BracketsLeft);
        put("]", Token.Type.BracketsRight);
        put("{", Token.Type.BracesLeft);
        put("}", Token.Type.BracesRight);

        put(",", Token.Type.Comma);
        put(":", Token.Type.Colon);
        put(";", Token.Type.Semicolon);
        put("-", Token.Type.Minus);
        put("+", Token.Type.Plus);
        put("*", Token.Type.Multiply);
        put("/", Token.Type.Divide);
    }};

    public final static HashMap<String, Token.Type> literalToType = new HashMap<>() {{
        put("true", Token.Type.BoolLiteral);
        put("false", Token.Type.BoolLiteral);

        put("Fighter", Token.Type.UnitLiteral);
        put("Destroyer", Token.Type.UnitLiteral);
        put("Carrier", Token.Type.UnitLiteral);
        put("Cruiser", Token.Type.UnitLiteral);
        put("Dreadnought", Token.Type.UnitLiteral);
        put("WarSun", Token.Type.UnitLiteral);
        put("Flagship", Token.Type.UnitLiteral);
        put("Infantry", Token.Type.UnitLiteral);
        put("SpaceDock", Token.Type.UnitLiteral);
        put("PDS", Token.Type.UnitLiteral);

        put("Red", Token.Type.ColorLiteral);
        put("Yellow", Token.Type.ColorLiteral);
        put("Green", Token.Type.ColorLiteral);
        put("Blue", Token.Type.ColorLiteral);
        put("Purple", Token.Type.ColorLiteral);
        put("Black", Token.Type.ColorLiteral);
        put("NoColor", Token.Type.ColorLiteral);
    }};

    public final static HashMap<String, Token.Type> keywordToType = new HashMap<>() {{
        put("int", Token.Type.Type);
        put("string", Token.Type.Type);
        put("bool", Token.Type.Type);
        put("unit", Token.Type.Type);
        put("color", Token.Type.Type);
        put("var", Token.Type.VarType);

        put("player", Token.Type.Player);
        put("planet", Token.Type.Planet);
        put("hex", Token.Type.Hex);
        put("move", Token.Type.UnitsAction);
        put("add", Token.Type.UnitsAction);
        put("remove", Token.Type.UnitsAction);
        put("from", Token.Type.From);
        put("to", Token.Type.To);
        put("has", Token.Type.Has);
        put("at", Token.Type.At);
        put("activated", Token.Type.Activated);
        put("activate", Token.Type.Activation);
        put("deactivate", Token.Type.Activation);

        put("foreach", Token.Type.Foreach);
        put("continue", Token.Type.Continue);
        put("break", Token.Type.Break);
        put("return", Token.Type.Return);
        put("print", Token.Type.Print);
        put("if", Token.Type.If);
        put("else", Token.Type.Else);
    }};
}
