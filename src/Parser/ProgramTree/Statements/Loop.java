package Parser.ProgramTree.Statements;

import Parser.ProgramTree.Block;
import Parser.ProgramTree.Variables.Variable;

public class Loop extends Statement{
    private Variable variable;
    private String arrayIdentifier;
    private Block block;
    public Loop(Variable variable, String arrayIdentifier, Block block){
        this.variable = variable;
        this.arrayIdentifier = arrayIdentifier;
        this.block = block;
    }
}
