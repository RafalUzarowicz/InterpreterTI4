package Utilities.ProgramTree.Value.BoardStateCheck;

import Utilities.ProgramTree.Value.Value;

public class HexStateCheck extends Value {
    private final Value unit;
    private final Value hex;

    public HexStateCheck(Value hex, Value unit){
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
        return "hex("+hex.toString()+")has("+unit.toString()+")";
    }
}
