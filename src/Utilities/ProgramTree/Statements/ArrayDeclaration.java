package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.Value.Value;
import Utilities.ProgramTree.Variables.Variable;

import java.util.ArrayList;

public class ArrayDeclaration extends Statement{
    private final ArrayList<Value> values;
    private final Variable type;
    private int size;
    private int count;

    public ArrayDeclaration(Variable type){
        this.type = type;
        this.values = new ArrayList<>();
        this.size = 0;
        this.count = 0;
    }
    public void add(Value value) throws Exception {
        values.add(value);
    }
    public void setSize(int size){
        this.size = size;
    }
    public int getSize(){
        return this.size;
    }
    public ArrayList<Value> getValues() {
        return values;
    }

    public String getIdentifier() {
        return type.getName();
    }

    public Variable getType() {
        return type;
    }
}
