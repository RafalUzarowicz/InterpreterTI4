package Parser.ProgramTree.Value.BoardStateCheck;

import Parser.ProgramTree.Value.Value;

public class PlanetStateCheck extends Value {
    private Value planet;
    private Value unit;

    public PlanetStateCheck(Value planet, Value unit){
        this.planet = planet;
        this.unit = unit;
    }
}
