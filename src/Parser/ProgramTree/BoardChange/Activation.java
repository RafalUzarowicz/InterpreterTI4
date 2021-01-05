package Parser.ProgramTree.BoardChange;

import Parser.ProgramTree.Value;

public class Activation extends PlayerAction{
    private Value where;
    private String action;
    public Activation(Value where, String aciton){
        this.where = where;
        this.action = aciton;
    }
}
