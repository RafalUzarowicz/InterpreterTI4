import Board.Board;
import Parser.Parser;
import Parser.ProgramTree.Program;
import Scanner.Scanner;
import Scanner.Token;
import Source.FileSource;
import Source.ResourceFileSource;
import Source.StringSource;
import Utilities.Dictionary;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Stack;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Main {
    public static void main(String[] args) {
        try {
//            FileSource source = new FileSource("code/simple.twlan");
            ResourceFileSource source = new ResourceFileSource("first.twlan");
//            StringSource source = new StringSource("int _main(){\nvar x_1d = !(2+3)*6--(7+2*3); var y = player(x)has(Fighter)at(h12);\n\tfuncall(2, 7);int[] x = int[2];}");
            Scanner scanner = new Scanner(source);

//            scanner.next();
//            while (scanner.peek().getType() != Token.Type.EOF) {
//                System.out.println(scanner.get());
//            }
            Parser parser = new Parser(scanner);
            Program program = parser.parse();

            System.out.print("TAK");

//             Board board = new Board();
//             board.loadState("board/boardstate.json");

//             System.out.println(board.hexes.get(0).getPlayerUnitNumber(Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter));
//             System.out.println(board.hexes.get(0).getPlayerUnitNumber(Dictionary.PlayerColors.Black, Dictionary.SpaceUnits.Fighter));
//             System.out.println(board.hexes.get(0).getActivation(Dictionary.PlayerColors.Red));
//
//             board.hexes.get(10).setPlayerUnitNumber(Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter, 99999999);
//
//             board.resetState();
//
//             board.saveState();


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
