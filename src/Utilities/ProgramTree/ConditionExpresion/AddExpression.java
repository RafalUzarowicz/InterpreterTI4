package Utilities.ProgramTree.ConditionExpresion;

import Utilities.ProgramTree.ConditionExpresion.Operators.Operator;
import Utilities.ProgramTree.INode;

import java.util.ArrayList;

public class AddExpression  implements INode {
    private final ArrayList<MultiplyExpression> multiplyExpressions;
    private final ArrayList<Operator> operators;
    public AddExpression(){
        multiplyExpressions = new ArrayList<>();
        operators = new ArrayList<>();
    }
    public void add(MultiplyExpression multiplyExpression){
        multiplyExpressions.add(multiplyExpression);
    }

    public void add(Operator operator, MultiplyExpression multiplyExpression){
        multiplyExpressions.add(multiplyExpression);
        operators.add(operator);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(multiplyExpressions.get(0).toString());

        for(int i = 1; i<multiplyExpressions.size(); ++i){
            stringBuilder.append(operators.get(i-1).toString());
            stringBuilder.append(multiplyExpressions.get(i).toString());
        }

        return stringBuilder.toString();
    }
}
