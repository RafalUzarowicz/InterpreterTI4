package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.Block;
import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Conditional extends Statement implements INode {
    private final OrCondition condition;
    private final Block ifBlock;
    private final Block elseBlock;
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

    public OrCondition getCondition() {
        return condition;
    }

    public Block getIfBlock() {
        return ifBlock;
    }

    public Block getElseBlock() {
        return elseBlock;
    }
}
