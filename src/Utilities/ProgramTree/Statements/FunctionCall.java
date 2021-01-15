package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.Arguments;
import Utilities.ProgramTree.INode;

public class FunctionCall extends Statement implements INode {
    private String identifier;
    private Arguments arguments;
    public FunctionCall(String identifier, Arguments arguments){
        this.identifier = identifier;
        this.arguments = arguments;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Arguments getArguments() {
        return arguments;
    }
}
