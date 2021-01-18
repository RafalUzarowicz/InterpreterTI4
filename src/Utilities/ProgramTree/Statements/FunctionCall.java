package Utilities.ProgramTree.Statements;

import Utilities.Position;
import Utilities.ProgramTree.Arguments;
import Utilities.ProgramTree.INode;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class FunctionCall extends Statement implements INode {
    private final String identifier;
    private final Arguments arguments;

    public FunctionCall(String identifier, Arguments arguments, Position position) {
        super(position);
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
