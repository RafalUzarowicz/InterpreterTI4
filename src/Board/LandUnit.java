package Board;

import Utilities.Dictionary;

import java.util.HashMap;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class that represents current board state.
 */
public class LandUnit {
    private final HashMap<Dictionary.LandUnits, Integer> units;
    public LandUnit(){
        units = new HashMap<>();
        for ( Dictionary.LandUnits unit : Dictionary.LandUnits.values() ) {
            units.put(unit, 0);
        }
    }

    public void setUnitNumber( Dictionary.LandUnits unit, int number ){
        units.put(unit, number);
    }

    public int getUnitNumber( Dictionary.LandUnits unit){
        return units.get(unit);
    }
}
