package Board;

import Utilities.Dictionary;

import java.util.HashMap;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class that represents current board state.
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
        return units.get(color).getUnitNumber(unit);
    }
}
