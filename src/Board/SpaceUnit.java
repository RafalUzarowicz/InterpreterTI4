package Board;

import Utilities.Dictionary;

import java.util.HashMap;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class that represents current board state.
 */
public class SpaceUnit {
    private final HashMap<Dictionary.SpaceUnits, Integer> units;
    public SpaceUnit(){
        units = new HashMap<>();
        for ( Dictionary.SpaceUnits unit : Dictionary.SpaceUnits.values() ) {
            units.put(unit, 0);
        }
    }

    public void setUnitNumber( Dictionary.SpaceUnits unit, int number ){
        units.put(unit, number);
    }

    public int getUnitNumber( Dictionary.SpaceUnits unit){
        return units.get(unit) != null?units.get(unit) : 0;
    }
}
