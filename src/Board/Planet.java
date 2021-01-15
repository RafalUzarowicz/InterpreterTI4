package Board;

import Utilities.Dictionary;

import java.util.HashMap;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class that represents single planet state.
 */
public class Planet {
    private HashMap<Dictionary.PlayerColors, LandUnit> units;

    public Planet(){
        units = new HashMap<>();
        for ( Dictionary.PlayerColors color : Dictionary.PlayerColors.values() ) {
            units.put(color, new LandUnit());
        }
    }

    public void setPlayerUnitNumber( Dictionary.PlayerColors color, Dictionary.LandUnits unit, int number ){
        units.get(color).setUnitNumber(unit, number);
    }

    public int getPlayerUnitNumber( Dictionary.PlayerColors color, Dictionary.LandUnits unit){
        LandUnit landUnit = units.get(color);
        if(landUnit != null){
            return landUnit.getUnitNumber(unit);
        }
        return 0;
    }

    public int getUnitState(Dictionary.LandUnits unit ){
        int sum = 0;
        for (var key : units.keySet() ) {
            LandUnit landUnit = units.get(key);
            if(landUnit != null){
                int unitCount = landUnit.getUnitNumber(unit);
                if(unit != null){
                    sum += unitCount;
                }
            }
        }
        return sum;
    }
}
