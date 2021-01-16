package Utilities.ProgramTree.Value.Literals;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Literal extends Value implements INode {
    protected String value;
    public Literal(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
