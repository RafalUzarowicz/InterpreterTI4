import Board.Board;
import Scanner.Scanner;
import Scanner.Token;
import Source.FileSource;
import Source.ResourceFileSource;
import Source.StringSource;
import Utilities.Dictionary;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Main {
    public static void main(String[] args) {
        try {
//            FileSource source = new FileSource("code/simple.twlan");
//            ResourceFileSource source = new ResourceFileSource("first.twlan");
//            StringSource source = new StringSource("int _main(){};\nvar x_1d = 999999;\n\tcolor v1_4 =h123");
//            Scanner scanner = new Scanner(source);
//            while (scanner.get().getType() != Token.Type.EOF) {
//                scanner.next();
//                System.out.println(scanner.get());
//            }

             Board board = new Board();
             board.loadState("board/boardstate.json");

//             System.out.println(board.hexes.get(0).getPlayerUnitNumber(Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter));
//             System.out.println(board.hexes.get(0).getPlayerUnitNumber(Dictionary.PlayerColors.Black, Dictionary.SpaceUnits.Fighter));
//             System.out.println(board.hexes.get(0).getActivation(Dictionary.PlayerColors.Red));
//
//             board.hexes.get(10).setPlayerUnitNumber(Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter, 99999999);
//
//             board.resetState();
//
             board.saveState();


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
