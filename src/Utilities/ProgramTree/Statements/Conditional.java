package Utilities.ProgramTree.Statements;

import Utilities.Position;
import Utilities.ProgramTree.Block;
import Utilities.ProgramTree.ConditionExpresion.Expression;
import Utilities.ProgramTree.INode;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Conditional extends Statement implements INode {
    private final Expression condition;
    private final Block ifBlock;
    private final Block elseBlock;

    public Conditional(Expression conditionExpression, Block block, Position position) {
        super(position);
        this.condition = conditionExpression;
        this.ifBlock = block;
        this.elseBlock = null;
    }

    public Conditional(Expression conditionExpression, Block ifBlock, Block elseBlock, Position position) {
        super(position);
        this.condition = conditionExpression;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public Expression getCondition() {
        return condition;
    }

    public Block getIfBlock() {
        return ifBlock;
    }

    public Block getElseBlock() {
        return elseBlock;
    }
}
