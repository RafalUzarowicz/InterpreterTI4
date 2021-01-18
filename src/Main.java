import Board.Board;
import Interpreter.Interpreter;
import Parser.Parser;
import Scanner.Scanner;
import Source.FileSource;
import Utilities.ProgramTree.ConditionExpresion.Expression;
import Utilities.ProgramTree.ConditionExpresion.OrCondition;
import Utilities.ProgramTree.Program;
import Utilities.ProgramTree.Value.Literals.BoolLiteral;
import Utilities.ProgramTree.Value.Literals.ColorLiteral;
import Utilities.ProgramTree.Value.Literals.IntLiteral;
import Utilities.ProgramTree.Value.Literals.Literal;
import Utilities.StringOutStream;
import Utilities.Token;
import Source.StringSource;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */

/*
* TODO:
*  - testy integracyjne do interpretera - duze kawalki kodu
*  - testy jednostkowe do interpretera - dla malych struktur
*  - testy ze sprawdzaniem zawartosci kontekstu
* */

public class Main {
    public static void main(String[] args) {
        try {

            StringSource source = new StringSource("" +
                    "int main(){" +
                        "var x_1d = !(2+3)*6--(7+2*3); " +
                        "player(Red)add(Fighter:2)to(h12);" +
                        "var y = player(Red)has(Fighter)at(h12) + 2; " +
                        "var z = 7-3; " +
                        "if(3<2){" +
                            "print(x_1d);" +
                            "z = 2;" +
                        "}" +
                        "else{" +
                            "print(y);" +
                            "z = 3;" +
                        "}" +
                        "print(z);" +
                        "return 0;" +
                    "}"
            );

//            FileSource source = new FileSource("code/simple.twlan");
//            ResourceFileSource source = new ResourceFileSource("first.twlan");
//            StringSource source = new StringSource("" +
//                    "int main(){\n" +
//                    "var x_1d = !(2+3)*6--(7+2*3); " +
//                    "int y = player(Red)has(Fighter)at(h12);\n\t" +
//                    "int[] x = int[2]; " +
//                    "" +
//                    "print(x_1d);}"
//            );
//            StringSource source = new StringSource("!(x+(!y+funCall(2,!(x[2]+(!y+funCall(2,4,x))+3),x))+3)");
//            StringSource source = new StringSource("hex(h1)has(Carrier)+2");
//            StringSource source = new StringSource("2");
//            StringSource source = new StringSource("int _main(){\nvar x_1d = !!!!!!x; }");
            Scanner scanner = new Scanner(source);

//            scanner.next();
//            while (scanner.peek().getType() != Token.Type.EOF) {
//                System.out.println(scanner.get());
//            }
            Parser parser = new Parser(scanner);


//            Expression orCondition = parser.tryOrCondition();
//            System.out.println(orCondition);
            Program program = parser.parse();
            Board board = new Board();
            StringOutStream stringOutStream = new StringOutStream();
            board.loadState("board/boardstate.json");

            Interpreter interpreter = new Interpreter(program, board, stringOutStream);

            interpreter.execute();

//             board.saveState();
//            System.out.print("TAK\n");
//            System.out.print(condition.toString());


//             System.out.println(board.hexes.get(0).getPlayerUnitNumber(Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter));
//             System.out.println(board.hexes.get(0).getPlayerUnitNumber(Dictionary.PlayerColors.Black, Dictionary.SpaceUnits.Fighter));
//             System.out.println(board.hexes.get(0).getActivation(Dictionary.PlayerColors.Red));
//
//             board.hexes.get(10).setPlayerUnitNumber(Dictionary.PlayerColors.Red, Dictionary.SpaceUnits.Fighter, 99999999);
//
//             board.resetState();
//

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
