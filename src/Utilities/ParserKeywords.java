package Utilities;

import Parser.ProgramTree.Variable;

import java.util.HashMap;

public class ParserKeywords {
    public final static HashMap<String, Variable.VariableType> keywordToVariableType = new HashMap<>() {{
        put("int", Variable.VariableType.Int);
        put("string", Variable.VariableType.String);
        put("bool", Variable.VariableType.Bool);
        put("unit", Variable.VariableType.Unit);
        put("color", Variable.VariableType.Color);
        put("var", Variable.VariableType.Var);
    }};
}
