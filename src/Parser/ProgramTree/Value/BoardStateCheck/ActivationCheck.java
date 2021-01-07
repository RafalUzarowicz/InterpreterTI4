package Parser.ProgramTree.Value.BoardStateCheck;

import Parser.ProgramTree.Value.Value;

public class ActivationCheck extends Value {
    private Value player;
    private Value hex;

    public ActivationCheck(Value player, Value hex){
        this.player = player;
        this.hex = hex;
    }
}
