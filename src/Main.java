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
//            ResourceFileSource source = new ResourceFileSource("first.twlan");
            StringSource source = new StringSource("int _main(){\nvar x_1d = !(2+3)*6--(7+2*3);\n\tfuncall(2, 7);int[] x = int[2];}");
            Scanner scanner = new Scanner(source);

//            scanner.next();
//            while (scanner.peek().getType() != Token.Type.EOF) {
//                System.out.println(scanner.get());
//            }
            Parser parser = new Parser(scanner);
            Program program = parser.parse();

            System.out.print("TAK");
//            Main main = new Main();
//            main.infixToTree();

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

    // Tree Structure
    public class Node{
        public Node(){
            this.data = ' ';
            left = right = null;
        }
        public Node(char c){
            this.data = c;
            left = right = null;
        }
        char data;
        Node left, right;
    }

    // Function to build Expression Tree
    Node build(String s)
    {

        // Stack to hold nodes
        Stack<Node> stN = new Stack<>();

        // Stack to hold chars
        Stack<Character> stC = new Stack<>();
        stC.push('(');
        Node t, t1, t2;

        // Prioritising the operators
        int []p = new int[123];
        p[')'] = 0;

        p['+'] = p['-'] = 3;
        p['/'] = p['*'] = 4;
        p['^'] = 5;

        for (int i = 0; i <= s.length(); i++)
        {
            char c = i >= s.length() ? ')' : s.charAt(i);

            if (c == '(') {

                // Push '(' in char stack
                stC.add(c);
            }

            // Push the operands in node stack
            else if (Character.isAlphabetic(c))
            {
                t = new Node(c);
                stN.add(t);
            }
            else if (p[c] > 0)
            {

                // If an operator with lower or
                // same associativity appears
                while (
                        !stC.isEmpty() && stC.peek() != '('
                                && ((c != '^' && p[stC.peek()] >= p[c])
                                || (c == '^'
                                && p[stC.peek()] > p[c])))
                {

                    // Get and remove the top element
                    // from the character stack
                    t = new Node(stC.peek());
                    stC.pop();

                    // Get and remove the top element
                    // from the node stack
                    t1 = stN.peek();
                    stN.pop();

                    // Get and remove the currently top
                    // element from the node stack
                    t2 = stN.peek();
                    stN.pop();

                    // Update the tree
                    t.left = t2;
                    t.right = t1;

                    // Push the node to the node stack
                    stN.add(t);
                }

                // Push s[i] to char stack
                stC.push(c);
            }
            else if (c == ')') {
                while (!stC.isEmpty() && stC.peek() != '(')
                {
                    t = new Node(stC.peek());
                    stC.pop();
                    t1 = stN.peek();
                    stN.pop();
                    t2 = stN.peek();
                    stN.pop();
                    t.left = t2;
                    t.right = t1;
                    stN.add(t);
                }
                stC.pop();
            }
        }
        t = stN.peek();
        return t;
    }

    // Function to print the post order
// traversal of the tree
    void postorder(Node root, int tabs)
    {
        if (root != null)
        {
//            if(root.left != null && root.right != null)
//            System.out.print("(");
            postorder(root.left, tabs+1);
            for(int i = 0; i<tabs; ++i){
                System.out.print("\t");
            }
            System.out.println(root.data);
            postorder(root.right, tabs + 1);
//            if(root.left != null && root.right != null)
//            System.out.print(")");
        }
    }

    // Driver code
    public void infixToTree()  {
        StringSource source = new StringSource("a+b*c+d");
        String s = "a+b*c+d";
//        N root = doIt(new Scanner(source));

        // Function call
//        postorder(root, 0);
    }

    public class N extends Node{

        public N() {

        }
    }

    public class Operator{
    }
    public class Value{
        public char name;
    }


    static final int COUNT = 10;

    static void print2DUtil(Node root, int space)
    {
        // Base case
        if (root == null)
            return;

        // Increase distance between levels
        space += COUNT;

        // Process right child first
        print2DUtil(root.right, space);

        // Print current node after space
        // count
        System.out.print("\n");
        for (int i = COUNT; i < space; i++)
            System.out.print(" ");
        System.out.print(root.data + "\n");

        // Process left child
        print2DUtil(root.left, space);
    }

    // Wrapper over print2DUtil()
    static void print2D(Node root)
    {
        // Pass initial space count as 0
        print2DUtil(root, 0);
    }
}
