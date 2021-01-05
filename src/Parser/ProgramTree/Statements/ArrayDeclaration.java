package Parser.ProgramTree.Statements;

import Parser.ProgramTree.Value;
import Parser.ProgramTree.Variable;

import java.util.ArrayList;

public class ArrayDeclaration extends Statement{
    private ArrayList<Value> values;
    private String identifier;
    private Variable.VariableType type;
    private int size;
    private int count;
    public ArrayDeclaration(Variable.VariableType type, String identifier){
        this.type = type;
        this.identifier = identifier;
        this.values = new ArrayList<>();
        this.size = 0;
        this.count = 0;
    }
    public void add(Value value) throws Exception {
        values.add(value);
        ++count;
        if( count >= size ){
            throw new Exception("Index out of bound.");
        }
    }
    public void setSize(int size){
        this.size = size;
    }
}
