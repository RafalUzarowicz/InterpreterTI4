package Utilities.ProgramTree.ConditionExpresion.Operators;

import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Operator implements INode {
    protected  String operator = "";

    @Override
    public String toString() {
        return operator;
    }
}
