package Utilities.ProgramTree;

import Utilities.ProgramTree.Statements.Statement;

import java.util.ArrayList;/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Block extends Statement  implements INode{
    private ArrayList<Statement> statements;
    public Block(){
        statements = new ArrayList<>();
    }
    public void add(Statement statement){
        statements.add(statement);
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }
}
