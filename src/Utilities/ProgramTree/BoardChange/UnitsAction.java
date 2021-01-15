package Utilities.ProgramTree.BoardChange;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;

public class UnitsAction extends PlayerAction implements INode {
    private Value fromWhere;
    private Value toWhere;
    private UnitsList unitsList;

    public UnitsAction(UnitsList unitsList, Value fromWhere, Value toWhere){
        this.unitsList = unitsList;
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
    }
}
