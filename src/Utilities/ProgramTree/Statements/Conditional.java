package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.Block;
import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.INode;

public class Conditional extends Statement implements INode {
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
