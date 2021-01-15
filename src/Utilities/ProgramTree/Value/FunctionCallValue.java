package Utilities.ProgramTree.Value;

import Utilities.ProgramTree.Arguments;

public class FunctionCallValue extends Value{
    private final Arguments arguments;
    private final String identifier;
    public FunctionCallValue(String identifier, Arguments arguments){
        this.identifier = identifier;
        this.arguments = arguments;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return identifier+"("+arguments.toString()+")";
    }
}
