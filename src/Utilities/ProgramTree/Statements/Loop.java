package Utilities.ProgramTree.Statements;

import Utilities.Position;
import Utilities.ProgramTree.Block;
import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Variables.Variable;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Loop extends Statement implements INode {
    private final Variable variable;
    private final String arrayIdentifier;
    private final Block block;
    public Loop(Variable variable, String arrayIdentifier, Block block, Position position){
        super(position);
        this.variable = variable;
        this.arrayIdentifier = arrayIdentifier;
        this.block = block;
    }

    public Variable getVariable() {
        return variable;
    }

    public String getArrayIdentifier() {
        return arrayIdentifier;
    }

    public Block getBlock() {
        return block;
    }
}
