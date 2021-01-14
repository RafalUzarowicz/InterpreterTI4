package Parser.ProgramTree.ConditionExpresion.Operators;

import Parser.ProgramTree.ConditionExpresion.Node;

public class Operator extends Node {
    protected  String operator = "";

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        if(left != null){
            stringBuilder.append(left.toString());
        }
        stringBuilder.append(operator);
        if(right != null){
            stringBuilder.append(right.toString());
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
