package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.INode;

public class Assignment extends Statement implements INode {
    private String index;
    private String identifier;
    private OrCondition value;
    public Assignment(String index, String identifier, OrCondition value){
        this.index = index;
        this.identifier = identifier;
        this.value = value;
    }

    public String getIndex() {
        return index;
    }

    public String getIdentifier() {
        return identifier;
    }

    public OrCondition getValue() {
        return value;
    }
}
