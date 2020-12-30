package Board;

import Utilities.Dictionary;

import java.util.HashMap;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class that represents current board state.
 */
public class Hex {
    private final HashMap<Dictionary.PlayerColors, Boolean> tokens;
    private final HashMap<Dictionary.PlayerColors, SpaceUnit> units;

    public Hex(){
        tokens = new HashMap<>();
        for ( Dictionary.PlayerColors color : Dictionary.PlayerColors.values() ) {
            tokens.put(color, false);
        }
        units = new HashMap<>();
        for ( Dictionary.PlayerColors color : Dictionary.PlayerColors.values() ) {
            units.put(color, new SpaceUnit());
        }
    }

    public void activate(Dictionary.PlayerColors color){
        tokens.put(color, true);
    }

    public void deactivate(Dictionary.PlayerColors color){
        tokens.put(color, false);
    }

    public void setActivation(Dictionary.PlayerColors color, boolean bool){
        tokens.put(color, bool);
    }

    public boolean getActivation(Dictionary.PlayerColors color){
        return tokens.get(color);
    }

    public void setPlayerUnitNumber( Dictionary.PlayerColors color, Dictionary.SpaceUnits unit, int number ){
        units.get(color).setUnitNumber(unit, number);
    }

    public int getPlayerUnitNumber( Dictionary.PlayerColors color, Dictionary.SpaceUnits unit){
        return units.get(color).getUnitNumber(unit);
    }
}
