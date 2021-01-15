package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.ConditionExpresion.Operators.Operator;
import Utilities.ProgramTree.INode;

import java.util.ArrayList;

public class RelationalCondition implements INode {
    ArrayList<AddExpression> addExpressions;
    ArrayList<Operator> operators;
    public RelationalCondition(){
        addExpressions = new ArrayList<>();
        operators = new ArrayList<>();
    }
    public void add(AddExpression addExpression){
        addExpressions.add(addExpression);
    }

    public void add(Operator operator, AddExpression addExpression){
        operators.add(operator);
        addExpressions.add(addExpression);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(addExpressions.get(0).toString());

        for(int i = 1; i<addExpressions.size(); ++i){
            stringBuilder.append(operators.get(i-1).toString());
            stringBuilder.append(addExpressions.get(i).toString());
        }

        return stringBuilder.toString();
    }
}
