package Parser.ProgramTree.Value.BoardStateCheck;

import Parser.ProgramTree.Value.Value;

public class ActivationCheck extends Value {
    private Value player;
    private Value hex;

    public ActivationCheck(Value player, Value hex){
        this.player = player;
        this.hex = hex;
    }

    public Value getPlayer() {
        return player;
    }

    public Value getHex() {
        return hex;
    }

    @Override
    public String toString() {
        return "player("+player.toString()+")activated("+hex.toString()+")";
    }
}
