package Parser.ProgramTree.Value.BoardStateCheck;

import Parser.ProgramTree.Value.Value;

public class HexStateCheck extends Value {
    private Value hex;
    private Value unit;

    public HexStateCheck(Value hex, Value unit){
        this.hex = hex;
        this.unit = unit;
    }
}
