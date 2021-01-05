package Utilities;

import java.util.HashMap;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Dictionary class used to represent board state.
 */
public class Dictionary {
    public enum PlayerColors{
        Red,
        Yellow,
        Green,
        Blue,
        Purple,
        Black
    }
    public enum SpaceUnits {
        Fighter,
        Destroyer,
        Carrier,
        Cruiser,
        Dreadnought,
        WarSun,
        Flagship,
        Infantry
    }

    public final static HashMap<SpaceUnits, String> SpaceUnitsEnumToLiteral = new HashMap<>() {{
        put(SpaceUnits.Fighter, "Fighter");
        put(SpaceUnits.Destroyer, "Destroyer");
        put(SpaceUnits.Carrier, "Carrier");
        put(SpaceUnits.Cruiser, "Cruiser");
        put(SpaceUnits.Dreadnought, "Dreadnought");
        put(SpaceUnits.WarSun, "WarSun");
        put(SpaceUnits.Flagship, "Flagship");
        put(SpaceUnits.Infantry, "Infantry");
    }};

    public enum LandUnits {
        Fighter,
        Infantry,
        SpaceDock,
        PDS
    }

    public final static HashMap<LandUnits, String> LandUnitsEnumToLiteral = new HashMap<>() {{
        put(LandUnits.Fighter, "Fighter");
        put(LandUnits.Infantry, "Infantry");
        put(LandUnits.SpaceDock, "SpaceDock");
        put(LandUnits.PDS, "PDS");
    }};
}