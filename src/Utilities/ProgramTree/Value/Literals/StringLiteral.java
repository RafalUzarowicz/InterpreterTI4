package Utilities.ProgramTree.Value.Literals;

import Utilities.ProgramTree.INode;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class StringLiteral extends Literal implements INode {
    public StringLiteral(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return "\"" + this.value + "\"";
    }
}
