package Parser.ProgramTree.ConditionExpresion;

import Parser.ProgramTree.Value.Value;

public class ValueNode extends Node{
    private final Value value;

    public ValueNode(Value value) {
        this.value = value;
        left = right = child = null;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
