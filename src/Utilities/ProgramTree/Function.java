package Utilities.ProgramTree;

import Utilities.ProgramTree.Variables.Variable;

public class Function {
    private Variable returnValue;
    private Parameters parameters;
    private String identifier;
    private Block block;

    public Function(Variable returnValue, String identifier, Parameters parameters, Block block){
        this.returnValue = returnValue;
        this.identifier = identifier;
        this.parameters = parameters;
        this.block = block;
    }

    public Variable getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Variable returnValue) {
        this.returnValue = returnValue;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
