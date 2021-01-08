package Parser.ProgramTree.BoardChange;

import Parser.ProgramTree.Value.Value;

public class Activation extends PlayerAction{
    private Value where;
    private String action;
    public Activation(Value where, String action){
        this.where = where;
        this.action = action;
    }
}
