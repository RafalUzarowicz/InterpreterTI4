package Utilities.ProgramTree.Value.BoardStateCheck;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class ActivationCheck extends Value implements INode {
    private final Value player;
    private final Value hex;

    public ActivationCheck(Value player, Value hex) {
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
        return "player(" + player.toString() + ")activated(" + hex.toString() + ")";
    }
}
