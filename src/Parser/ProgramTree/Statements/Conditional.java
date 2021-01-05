package Parser.ProgramTree.Statements;

import Parser.ProgramTree.Block;
import Parser.ProgramTree.ConditionExpresion.OrCondition;

public class Conditional extends Statement{
    private OrCondition condition;
    private Block ifBlock;
    private Block elseBlock;
    public Conditional(OrCondition orCondition, Block block){
        this.condition = orCondition;
        this.ifBlock = block;
        this.elseBlock = null;
    }

    public Conditional(OrCondition orCondition, Block ifBlock, Block elseBlock){
        this.condition = orCondition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }
}
