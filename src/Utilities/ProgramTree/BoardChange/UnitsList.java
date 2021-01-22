package Utilities.ProgramTree.BoardChange;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;

import java.util.ArrayList;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class UnitsList implements INode {
    public class UnitAmount {
        private final Value unit;
        private final Value amount;

        public UnitAmount(Value unit, Value amount) {
            this.unit = unit;
            this.amount = amount;
        }

        public Value getUnit() {
            return unit;
        }

        public Value getAmount() {
            return amount;
        }
    }

    private final ArrayList<UnitAmount> unitAmounts;

    public UnitsList() {
        unitAmounts = new ArrayList<>();
    }

    public void add(Value unit, Value amount) {
        unitAmounts.add(new UnitAmount(unit, amount));
    }

    public ArrayList<UnitAmount> getUnitAmounts() {
        return unitAmounts;
    }
}
