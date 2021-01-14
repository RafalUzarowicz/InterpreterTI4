package Parser.ProgramTree.ConditionExpresion;

import java.util.ArrayList;

public class AddExpression {
    private ArrayList<MultiplyExpression> multiplyExpressions;
    public AddExpression(){
        multiplyExpressions = new ArrayList<>();
    }
    public void add(MultiplyExpression multiplyExpression){
        multiplyExpressions.add(multiplyExpression);
    }
}
