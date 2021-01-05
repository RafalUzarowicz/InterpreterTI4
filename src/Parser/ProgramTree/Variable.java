package Parser.ProgramTree;

import Parser.Parser;
import Scanner.Token;

public class Variable {
    public enum VariableType{
        Int,
        String,
        Bool,
        Unit,
        Color,
        Hex,
        Planet,
        Var
    }
    private String value;
    private VariableType type;
    private String name;
    public Variable(VariableType type){
        this.type = type;
        name = null;
    }
    public Variable(VariableType type, String name){
        this.type = type;
        this.name = name;
    }
}
