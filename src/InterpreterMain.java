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
public class InterpreterMain {
    public static void main(String[] args) {
        if(args.length == 2){
            try {
                FileSource source = new FileSource(args[0]);
                Scanner scanner = new Scanner(source);
                Parser parser = new Parser(scanner);
                Program program = parser.parse();
                Board board = new Board();
                StringOutStream stringOutStream = new StringOutStream();
                board.loadState(args[1]);

                Interpreter interpreter = new Interpreter(program, board, stringOutStream);

                interpreter.execute(false);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }else{
            System.err.println("Wrong number of files.");
            System.exit(1);
        }
    }
}
