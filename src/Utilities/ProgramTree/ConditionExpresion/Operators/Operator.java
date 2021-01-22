package Utilities.ProgramTree.ConditionExpresion.Operators;

import Utilities.ProgramTree.INode;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Operator implements INode {
    protected String operator = "";

    public Operator(String operator) {
        this.operator = operator;
    }

    public Operator() {

    }

    @Override
    public String toString() {
        return operator;
    }
}
