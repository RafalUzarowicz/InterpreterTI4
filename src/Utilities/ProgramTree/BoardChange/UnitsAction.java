package Utilities.ProgramTree.BoardChange;

import Utilities.ProgramTree.Value.Value;

public class UnitsAction extends PlayerAction{
    private Value fromWhere;
    private Value toWhere;
    private UnitsList unitsList;

    public UnitsAction(UnitsList unitsList, Value fromWhere, Value toWhere){
        this.unitsList = unitsList;
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
    }
}
