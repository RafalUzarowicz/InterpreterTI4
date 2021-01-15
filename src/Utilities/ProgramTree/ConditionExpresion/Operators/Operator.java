package Utilities.ProgramTree.ConditionExpresion.Operators;

import Utilities.ProgramTree.INode;

public class Operator implements INode {
    protected  String operator = "";

    @Override
    public String toString() {
        return operator;
    }
}
