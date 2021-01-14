package Parser.ProgramTree.ConditionExpresion;

import java.util.ArrayList;

public class MultiplyExpression {
    private ArrayList<UnaryExpression> expressions;
    public MultiplyExpression(){
        expressions = new ArrayList<>();
    }
    public void add(UnaryExpression expression){
        expressions.add(expression);
    }
}
