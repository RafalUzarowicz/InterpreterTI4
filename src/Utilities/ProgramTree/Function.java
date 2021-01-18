package Utilities.ProgramTree;

import Utilities.Position;
import Utilities.ProgramTree.Variables.Variable;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Function implements INode{
    private final Variable returnValue;
    private final Parameters parameters;
    private final String identifier;
    private final Block block;
    private final Position position;

    public Function(Variable returnValue, String identifier, Parameters parameters, Block block, Position position){
        this.returnValue = returnValue;
        this.identifier = identifier;
        this.parameters = parameters;
        this.block = block;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Variable getReturnValue() {
        return returnValue;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Block getBlock() {
        return block;
    }
}
