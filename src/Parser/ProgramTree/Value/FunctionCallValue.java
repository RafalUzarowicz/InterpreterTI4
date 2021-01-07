package Parser.ProgramTree.Value;

import Parser.ProgramTree.Statements.Arguments;

public class FunctionCallValue extends Value{
    private String identifier;
    private Arguments arguments;
    public FunctionCallValue(String identifier, Arguments arguments){
        this.identifier = identifier;
        this.arguments = arguments;
    }
}
