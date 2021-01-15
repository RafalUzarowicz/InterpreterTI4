package Utilities.ProgramTree.Value.Literals;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;

public class Literal extends Value implements INode {
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
