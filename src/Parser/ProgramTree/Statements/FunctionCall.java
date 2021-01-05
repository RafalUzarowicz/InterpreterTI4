package Parser.ProgramTree.Statements;

public class FunctionCall extends Statement{
    private String identifier;
    private Arguments arguments;
    public FunctionCall(String identifier, Arguments arguments){
        this.identifier = identifier;
        this.arguments = arguments;
    }
}
