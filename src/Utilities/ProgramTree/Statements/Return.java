package Utilities.ProgramTree.Statements;

import Utilities.Position;
import Utilities.ProgramTree.ConditionExpresion.Expression;
import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Return extends Statement implements INode {
    private final Expression returnValue;
    public Return(Expression conditionExpression, Position position){
        super(position);
        returnValue = conditionExpression;
    }

    public Expression getReturnValue() {
        return returnValue;
    }
}
