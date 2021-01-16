package Utilities.ProgramTree.Value;

import Utilities.ProgramTree.Arguments;
import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class FunctionCallValue extends Value implements INode {
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
