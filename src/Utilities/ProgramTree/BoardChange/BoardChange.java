package Utilities.ProgramTree.BoardChange;

import Utilities.Position;
import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Statements.Statement;
import Utilities.ProgramTree.Value.Value;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class BoardChange extends Statement implements INode {
    private final Value player;
    private final PlayerAction playerAction;

    public BoardChange(Value player, PlayerAction playerAction, Position position) {
        super(position);
        this.player = player;
        this.playerAction = playerAction;
    }

    public Value getPlayer() {
        return player;
    }

    public PlayerAction getPlayerAction() {
        return playerAction;
    }
}
