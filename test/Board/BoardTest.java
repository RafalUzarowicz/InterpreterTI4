package Board;

import Utilities.Dictionary;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void getPlanetState() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        int state = board.getPlanetState(0, Dictionary.LandUnits.Fighter);
        // Assert
        assertEquals(1, state);
    }

    @Test
    public void getHexState() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        int state = board.getHexState(0, Dictionary.SpaceUnits.Fighter);
        // Assert
        assertEquals(1, state);
    }

    @Test
    public void getPlayerPlanetUnitNumber() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        int state = board.getPlayerPlanetUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter);
        // Assert
        assertEquals(1, state);
    }

    @Test
    public void getPlayerHexUnitNumber() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        int state = board.getPlayerHexUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter);
        // Assert
        assertEquals(1, state);
    }

    @Test
    public void changePlayerPlanetUnitNumber() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        int before = board.getPlayerPlanetUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter);
        board.changePlayerPlanetUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter, 2);
        int after = board.getPlayerPlanetUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.LandUnits.Fighter);
        // Assert
        assertNotEquals(before, after);
        assertEquals(1, before);
        assertEquals(3, after);
    }

    @Test
    public void changePlayerHexUnitNumber() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        int before = board.getPlayerHexUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter);
        board.changePlayerHexUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter, 2);
        int after = board.getPlayerHexUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter);
        // Assert
        assertNotEquals(before, after);
        assertEquals(1, before);
        assertEquals(3, after);
    }

    @Test
    public void isActivated() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        boolean activation = board.isActivated(0, Dictionary.PlayerColors.Red);
        // Assert
        assertTrue(activation);
    }

    @Test
    public void deactivate() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        board.deactivate(0, Dictionary.PlayerColors.Red);
        boolean activation = board.isActivated(0, Dictionary.PlayerColors.Red);
        // Assert
        assertFalse(activation);
    }

    @Test
    public void activate() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        board.deactivate(0, Dictionary.PlayerColors.Red);
        // Act
        board.activate(0, Dictionary.PlayerColors.Red);
        boolean activation = board.isActivated(0, Dictionary.PlayerColors.Red);
        // Assert
        assertTrue(activation);
    }

    @Test
    public void resetFileState() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        Board boardTemp1 = new Board();
        Board boardTemp2 = new Board();
        board.loadState("test/Board/testBoard.json");
        boardTemp2.loadState("test/Board/testBoard.json");
        // Act
        int before = board.getHexState(0, Dictionary.SpaceUnits.Fighter);
        board.resetFileState();
        boardTemp1.loadState("test/Board/testBoard.json");
        int after = boardTemp1.getHexState(0, Dictionary.SpaceUnits.Fighter);
        boardTemp2.saveState();
        // Assert
        assertEquals(1, before);
        assertEquals(0, after);
    }

    @Test
    public void saveState() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        int before = board.getHexState(0, Dictionary.SpaceUnits.Fighter);
        board.changePlayerHexUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter, 2);
        board.saveState();
        board.reloadState();
        int after = board.getHexState(0, Dictionary.SpaceUnits.Fighter);
        board.changePlayerHexUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter, -2);
        board.saveState();
        // Assert
        assertEquals(1, before);
        assertEquals(3, after);
    }

    @Test
    public void loadState() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        int pState = board.getPlanetState(0, Dictionary.LandUnits.Fighter);
        int hState = board.getHexState(0, Dictionary.SpaceUnits.Fighter);
        // Assert
        assertEquals(1, pState);
        assertEquals(1, hState);
    }

    @Test
    public void reloadState() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        int before = board.getHexState(0, Dictionary.SpaceUnits.Fighter);
        board.changePlayerHexUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter, 2);
        board.reloadState();
        int after = board.getPlanetState(0, Dictionary.LandUnits.Fighter);
        // Assert
        assertEquals(1, before);
        assertEquals(1, after);
    }

    @Test
    public void resetState() throws IOException, ParseException {
        // Arrange
        Board board = new Board();
        board.loadState("test/Board/testBoard.json");
        // Act
        board.changePlayerHexUnitNumber(0, Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter, 2);
        int before = board.getHexState(0, Dictionary.SpaceUnits.Fighter);
        board.resetState();
        int after = board.getHexState(0, Dictionary.SpaceUnits.Fighter);
        // Assert
        assertNotEquals(after, before);
        assertEquals(0, after);
    }
}
