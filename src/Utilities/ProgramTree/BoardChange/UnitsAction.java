package Utilities.ProgramTree.BoardChange;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class UnitsAction extends PlayerAction implements INode {
    private final Value fromWhere;
    private final Value toWhere;
    private final UnitsList unitsList;

    public UnitsAction(UnitsList unitsList, Value fromWhere, Value toWhere){
        this.unitsList = unitsList;
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
    }

    public Value getFromWhere() {
        return fromWhere;
    }

    public Value getToWhere() {
        return toWhere;
    }

    public UnitsList getUnitsList() {
        return unitsList;
    }
}
