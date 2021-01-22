package Utilities.ProgramTree.Value.BoardStateCheck;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class PlanetStateCheck extends Value implements INode {
    private final Value unit;
    private final Value planet;

    public PlanetStateCheck(Value planet, Value unit) {
        this.planet = planet;
        this.unit = unit;
    }

    public Value getPlanet() {
        return planet;
    }

    public Value getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "planet(" + planet.toString() + ")has(" + unit.toString() + ")";
    }
}
