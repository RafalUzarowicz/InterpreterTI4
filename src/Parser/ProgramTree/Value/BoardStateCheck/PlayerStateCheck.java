package Parser.ProgramTree.Value.BoardStateCheck;

import Parser.ProgramTree.Value.Value;

public class PlayerStateCheck extends Value {
    public Value getPlayer() {
        return player;
    }

    public Value getUnit() {
        return unit;
    }

    public Value getPlace() {
        return place;
    }

    private final Value player;
    private final Value unit;
    private final Value place;

    public PlayerStateCheck(Value player, Value unit, Value place){
        this.player = player;
        this.place = place;
        this.unit = unit;
    }


}
