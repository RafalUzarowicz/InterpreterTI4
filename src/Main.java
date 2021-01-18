import Board.Board;
import Interpreter.Interpreter;
import Parser.Parser;
import Scanner.Scanner;
import Source.FileSource;
import Utilities.ProgramTree.Program;
import Utilities.StringOutStream;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Main {
    public static void main(String[] args) {
        try {

            FileSource source = new FileSource("code/simple.twlan");
            Scanner scanner = new Scanner(source);
            Parser parser = new Parser(scanner);
            Program program = parser.parse();
            Board board = new Board();
            StringOutStream stringOutStream = new StringOutStream();
            board.loadState("board/boardstate.json");

            Interpreter interpreter = new Interpreter(program, board, stringOutStream);

            interpreter.execute(false);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
