package Parser.ProgramTree.Value.BoardStateCheck;

import Parser.ProgramTree.Value.Value;

public class PlayerStateCheck extends Value {
    private Value player;
    private Value unit;
    private Value place;

    public PlayerStateCheck(Value player, Value unit, Value place){
        this.player = player;
        this.place = place;
        this.unit = unit;
    }
}
