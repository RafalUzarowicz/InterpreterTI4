package Board;

import Utilities.Constants;
import Utilities.Dictionary;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * <p>
 * Class that represents current board state.
 */
public class Board {
    public ArrayList<Hex> hexes;
    public ArrayList<Planet> planets;

    private String boardFile;
    public Board(){
        hexes = new ArrayList<>(Constants.Board.HEX_NUMBER);
        for ( int i = 0; i < Constants.Board.HEX_NUMBER; ++i ) {
            hexes.add(new Hex());
        }
        planets = new ArrayList<>(Constants.Board.PLANET_NUMBER);
        for ( int i = 0; i < Constants.Board.PLANET_NUMBER; ++i ) {
            planets.add(new Planet());
        }
        boardFile = "boardFile.json";
    }

    public void reloadState() throws IOException, ParseException {
        loadState(boardFile);
    }

    public void loadState(String file) throws IOException, ParseException {
        boardFile = file;
        FileReader reader = new FileReader(boardFile);
        JSONParser parser = new JSONParser();
        JSONObject boardState = (JSONObject) parser.parse(reader);

        // Hexes
        JSONObject hexesData;
        try{
            hexesData = (JSONObject) boardState.get("hexes");
            for( int i = 0; i < Constants.Board.HEX_NUMBER; ++i ){
                JSONObject hexData;
                try {
                    hexData = (JSONObject) hexesData.get("h"+i);
                    // Tokens
                    JSONObject tokensData;
                    try {
                        tokensData = (JSONObject) hexData.get("tokens");
                        for (Dictionary.PlayerColors color: Dictionary.PlayerColors.values() ) {
                            boolean isActivated;
                            try{
                                isActivated = (boolean) tokensData.get(color.name());
                            }catch (NullPointerException e){
                                isActivated = false;
                            }
                            hexes.get(i).setActivation(color, isActivated);
                        }
                    }catch (NullPointerException ignored){}
                    // Units
                    JSONObject playerUnitsData;
                    for (Dictionary.PlayerColors color: Dictionary.PlayerColors.values() ) {
                        try{
                            playerUnitsData = (JSONObject) hexData.get(color.name());
                            for (Dictionary.SpaceUnits unit : Dictionary.SpaceUnits.values()){
                                int number;
                                try{
                                    number = Integer.parseInt(playerUnitsData.get(unit.name()).toString());
                                }catch (NullPointerException e){
                                    number = 0;
                                }
                                hexes.get(i).setPlayerUnitNumber(color, unit, number);
                            }
                        }catch (NullPointerException ignored){}
                    }
                }catch (NullPointerException ignored){}
            }
        }catch (NullPointerException ignored){}
        // Planets
        JSONObject planetsData;
        try {
            planetsData = (JSONObject) boardState.get("planets");
            for( int i = 0; i < Constants.Board.PLANET_NUMBER; ++i ){
                JSONObject planetData;
                try{
                    planetData = (JSONObject) planetsData.get("p"+i);
                    // Units
                    JSONObject playerUnitsData;
                    for (Dictionary.PlayerColors color: Dictionary.PlayerColors.values() ) {
                        try {
                            playerUnitsData = (JSONObject) planetData.get(color.name());
                            for (Dictionary.LandUnits unit : Dictionary.LandUnits.values()){
                                int number;
                                try{
                                    number = Integer.parseInt(playerUnitsData.get(unit.name()).toString());
                                }catch (NullPointerException e){
                                    number = 0;
                                }
                                planets.get(i).setPlayerUnitNumber(color, unit, number);
                            }
                        }catch (NullPointerException ignored){}
                    }
                }catch (NullPointerException ignored){}
            }
        }catch (NullPointerException ignored){}
    }

    public void saveState() throws IOException {
        JSONObject boardState = new JSONObject();

        // Hexes
        JSONObject hexesData = new JSONObject();

        for( int i = 0; i < Constants.Board.HEX_NUMBER; ++i ){
            JSONObject hexData = new JSONObject();
            // Tokens
            JSONObject tokensData = new JSONObject();
            for (Dictionary.PlayerColors color: Dictionary.PlayerColors.values() ) {
                if(hexes.get(i).getActivation(color)){
                    tokensData.put(color.name(), hexes.get(i).getActivation(color));
                }
            }
            if(!tokensData.isEmpty()){
                hexData.put("tokens", tokensData);
            }
            // Units
            JSONObject playerUnitsData;
            for (Dictionary.PlayerColors color: Dictionary.PlayerColors.values() ) {
                playerUnitsData = new JSONObject();
                for (Dictionary.SpaceUnits unit : Dictionary.SpaceUnits.values()){
                    if(hexes.get(i).getPlayerUnitNumber(color, unit)>0){
                        playerUnitsData.put(unit.name(), hexes.get(i).getPlayerUnitNumber(color, unit));
                    }
                }
                if(!playerUnitsData.isEmpty()){
                    hexData.put(color.name(), playerUnitsData);
                }
            }
            if(!hexData.isEmpty()){
                hexesData.put("h"+i, hexData);
            }
        }
        if(!hexesData.isEmpty()){
            boardState.put("hexes", hexesData);
        }

        // Planets
        JSONObject planetsData = new JSONObject();

        for( int i = 0; i < Constants.Board.PLANET_NUMBER; ++i ){
            JSONObject planetData = new JSONObject();
            // Units
            JSONObject playerUnitsData;
            for (Dictionary.PlayerColors color: Dictionary.PlayerColors.values() ) {
                playerUnitsData = new JSONObject();
                for (Dictionary.LandUnits unit : Dictionary.LandUnits.values()){
                    if(planets.get(i).getPlayerUnitNumber(color, unit)>0){
                        playerUnitsData.put(unit.name(), planets.get(i).getPlayerUnitNumber(color, unit));
                    }
                }
                if(!playerUnitsData.isEmpty()){
                    planetData.put(color.name(), playerUnitsData);
                }
            }
            if(!planetData.isEmpty()){
                planetsData.put("p"+i, planetData);
            }
        }
        if(!planetsData.isEmpty()){
            boardState.put("planets", planetsData);
        }

        // Save
        Files.write(Paths.get(boardFile), boardState.toJSONString().getBytes());
    }

    public void resetState(){
        // Hexes
        for( int i = 0; i < Constants.Board.HEX_NUMBER; ++i ){
            // Tokens
            for (Dictionary.PlayerColors color: Dictionary.PlayerColors.values() ) {
                hexes.get(i).setActivation(color, false);
            }
            // Units
            for (Dictionary.PlayerColors color: Dictionary.PlayerColors.values() ) {
                for (Dictionary.SpaceUnits unit : Dictionary.SpaceUnits.values()){
                    hexes.get(i).setPlayerUnitNumber(color, unit, 0);
                }
            }
        }

        // Planets
        for( int i = 0; i < Constants.Board.PLANET_NUMBER; ++i ){
            // Units
            for (Dictionary.PlayerColors color: Dictionary.PlayerColors.values() ) {
                for (Dictionary.LandUnits unit : Dictionary.LandUnits.values()){
                    planets.get(i).setPlayerUnitNumber(color, unit, 0);
                }
            }
        }
    }

    public void resetFileState() throws IOException {
        JSONObject boardState = new JSONObject();

        Files.write(Paths.get(boardFile), boardState.toJSONString().getBytes());
    }

    public void activate(int index, Dictionary.PlayerColors color){
        hexes.get(index).activate(color);
    }

    public void deactivate(int index, Dictionary.PlayerColors color){
        hexes.get(index).deactivate(color);
    }
}
