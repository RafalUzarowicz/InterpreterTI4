package Parser.ProgramTree.Statements;

import Parser.ProgramTree.Block;
import Parser.ProgramTree.ConditionExpresion.ConditionExpression;

public class Conditional extends Statement{
    private ConditionExpression condition;
    private Block ifBlock;
    private Block elseBlock;
    public Conditional(ConditionExpression conditionExpression, Block block){
        this.condition = conditionExpression;
        this.ifBlock = block;
        this.elseBlock = null;
    }

    public Conditional(ConditionExpression conditionExpression, Block ifBlock, Block elseBlock){
        this.condition = conditionExpression;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }
}
