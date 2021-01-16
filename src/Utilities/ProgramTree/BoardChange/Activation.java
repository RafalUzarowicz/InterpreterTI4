package Utilities.ProgramTree.BoardChange;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Activation extends PlayerAction implements INode {
    private Value where;
    private String action;
    public Activation(Value where, String action){
        this.where = where;
        this.action = action;
    }

    public Value getWhere() {
        return where;
    }

    public String getAction() {
        return action;
    }
}
