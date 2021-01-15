package Utilities.ProgramTree.BoardChange;

import Utilities.ProgramTree.Statements.Statement;
import Utilities.ProgramTree.Value.Value;

public class BoardChange extends Statement {
    private Value player;
    private PlayerAction playerAction;
    public BoardChange(Value player, PlayerAction playerAction){
        this.player = player;
        this.playerAction = playerAction;
    }
}
