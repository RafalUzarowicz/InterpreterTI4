import Parser.Parser;
import Parser.ProgramTree.ConditionExpresion.OrCondition;
import Parser.ProgramTree.Program;
import Scanner.Scanner;
import Scanner.Token;
import Source.FileSource;
import Source.StringSource;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Main {
    public static void main(String[] args) {
        try {
//            FileSource source = new FileSource("code/simple.twlan");
//            ResourceFileSource source = new ResourceFileSource("first.twlan");
//            StringSource source = new StringSource("int _main(){\nvar x_1d = !(2+3)*6--(7+2*3); var y = player(x)has(Fighter)at(h12);\n\tfuncall(2, 7);int[] x = int[2];}");
//            StringSource source = new StringSource("!(x+(!y+funCall(2,!(x[2]+(!y+funCall(2,4,x))+3),x))+3)");
            StringSource source = new StringSource("hex(h1)has(Carrier)+2");
//            StringSource source = new StringSource("int _main(){\nvar x_1d = !!!!!!x; }");
            Scanner scanner = new Scanner(source);

//            scanner.next();
            while (scanner.peek().getType() != Token.Type.EOF) {
                System.out.println(scanner.get());
            }
//            Parser parser = new Parser(scanner);
//            OrCondition condition = parser.tryOrCondition();


//            Program program = parser.parse();

            System.out.print("TAK\n");
//            System.out.print(condition.toString());

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
