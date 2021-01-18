package Utilities.ProgramTree.Statements;

import Utilities.Position;
import Utilities.ProgramTree.ConditionExpresion.Expression;
import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Assignment extends Statement implements INode {
    private final String index;
    private final String identifier;
    private final Expression value;
    public Assignment(String index, String identifier, Expression value, Position position){
        super(position);
        this.index = index;
        this.identifier = identifier;
        this.value = value;
    }

    public String getIndex() {
        return index;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getValue() {
        return value;
    }
}
