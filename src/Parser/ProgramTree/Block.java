package Parser.ProgramTree;

import Parser.Parser;
import Parser.ProgramTree.Statements.Statement;

import java.util.ArrayList;

public class Block extends Statement {
    private ArrayList<Statement> statements;
    public Block(){
        statements = new ArrayList<>();
    }
    public void add(Statement statement){
        statements.add(statement);
    }
}
