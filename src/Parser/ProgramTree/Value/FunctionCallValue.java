package Parser.ProgramTree.Value;

import Parser.ProgramTree.Arguments;

public class FunctionCallValue extends Value{
    public String getIdentifier() {
        return identifier;
    }

    public Arguments getArguments() {
        return arguments;
    }

    private final Arguments arguments;
    private final String identifier;
    public FunctionCallValue(String identifier, Arguments arguments){
        this.identifier = identifier;
        this.arguments = arguments;
    }
}
