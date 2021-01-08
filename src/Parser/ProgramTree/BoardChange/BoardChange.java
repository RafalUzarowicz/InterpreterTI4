package Parser.ProgramTree.BoardChange;

import Parser.ProgramTree.Statements.Statement;
import Parser.ProgramTree.Value.Value;

public class BoardChange extends Statement {
    private Value player;
    private PlayerAction playerAction;
    public BoardChange(Value player, PlayerAction playerAction){
        this.player = player;
        this.playerAction = playerAction;
    }
}
