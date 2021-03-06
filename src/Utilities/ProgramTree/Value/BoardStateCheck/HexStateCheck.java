package Utilities.ProgramTree.Value.BoardStateCheck;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class HexStateCheck extends Value implements INode {
    private final Value unit;
    private final Value hex;

    public HexStateCheck(Value hex, Value unit) {
        this.hex = hex;
        this.unit = unit;
    }

    public Value getHex() {
        return hex;
    }

    public Value getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "hex(" + hex.toString() + ")has(" + unit.toString() + ")";
    }
}
