package Utilities.ProgramTree.Statements;

import Utilities.Position;
import Utilities.ProgramTree.ConditionExpresion.Expression;
import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.INode;

import java.util.ArrayList;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Print extends Statement implements INode {
    ArrayList<Expression> conditionExpressions;
    public Print(Position position){
        super(position);
        conditionExpressions = new ArrayList<>();
    }
    public void add(Expression conditionExpression){
        conditionExpressions.add(conditionExpression);
    }

    public ArrayList<Expression> getConditionExpressions() {
        return conditionExpressions;
    }
}
