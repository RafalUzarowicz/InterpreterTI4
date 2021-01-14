package Parser.ProgramTree.Value.Literals;

import Parser.ProgramTree.Value.Value;

public class Literal extends Value {
    private String value;
    public Literal(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
