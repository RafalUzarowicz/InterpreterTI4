package Parser.ProgramTree.Statements;

import Parser.ProgramTree.Block;
import Parser.ProgramTree.ConditionExpresion.OrCondition;

public class Conditional extends Statement{
    private OrCondition condition;
    private Block ifBlock;
    private Block elseBlock;
    public Conditional(OrCondition conditionExpression, Block block){
        this.condition = conditionExpression;
        this.ifBlock = block;
        this.elseBlock = null;
    }

    public Conditional(OrCondition conditionExpression, Block ifBlock, Block elseBlock){
        this.condition = conditionExpression;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }
}
