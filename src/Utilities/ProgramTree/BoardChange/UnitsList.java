package Utilities.ProgramTree.BoardChange;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;

import java.util.ArrayList;

public class UnitsList implements INode {
    public class UnitAmount{
        private Value unit;
        private Value amount;
        public UnitAmount(Value unit, Value amount){
            this.unit = unit;
            this.amount = amount;
        }
    }

    ArrayList<UnitAmount> unitAmounts;
    public UnitsList(){
        unitAmounts = new ArrayList<>();
    }
    public void add(Value unit, Value amount){
        unitAmounts.add(new UnitAmount(unit, amount));
    }
}
