package Board;

import Utilities.Dictionary;

import java.util.HashMap;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class that represents single hex state.
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

    public void changePlayerUnitNumber( Dictionary.PlayerColors color, Dictionary.SpaceUnits unit, int amount){
        SpaceUnit spaceUnit = units.get(color);
        if(spaceUnit != null){
            spaceUnit.setUnitNumber(unit, spaceUnit.getUnitNumber(unit) + amount);
        }
    }

    public int getPlayerUnitNumber( Dictionary.PlayerColors color, Dictionary.SpaceUnits unit){
        SpaceUnit spaceUnit = units.get(color);
        if(spaceUnit != null){
            return spaceUnit.getUnitNumber(unit);
        }
        return 0;
    }

    public int getUnitState(Dictionary.SpaceUnits unit ){
        int sum = 0;
        for (var key : units.keySet() ) {
            SpaceUnit spaceUnit = units.get(key);
            if(spaceUnit != null){
                int unitCount = spaceUnit.getUnitNumber(unit);
                if(unit != null){
                    sum += unitCount;
                }
            }
        }
        return sum;
    }
}
