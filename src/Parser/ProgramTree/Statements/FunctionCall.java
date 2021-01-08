package Parser.ProgramTree.Statements;

import Parser.ProgramTree.Arguments;

public class FunctionCall extends Statement {
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
