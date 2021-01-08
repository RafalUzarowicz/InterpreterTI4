package Parser.ProgramTree.Value.BoardStateCheck;

import Parser.ProgramTree.Value.Value;

public class HexStateCheck extends Value {
    public Value getHex() {
        return hex;
    }

    public Value getUnit() {
        return unit;
    }

    private Value unit;
    private Value hex;

    public HexStateCheck(Value hex, Value unit){
        this.hex = hex;
        this.unit = unit;
    }
}
