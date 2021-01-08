package Parser.ProgramTree.Value.BoardStateCheck;

import Parser.ProgramTree.Value.Value;

public class PlanetStateCheck extends Value {
    public Value getPlanet() {
        return planet;
    }

    public Value getUnit() {
        return unit;
    }

    private Value unit;
    private Value planet;

    public PlanetStateCheck(Value planet, Value unit){
        this.planet = planet;
        this.unit = unit;
    }
}
