package Utilities.ProgramTree.Statements;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Value;
import Utilities.ProgramTree.Variables.ArrayVariable;

import java.util.ArrayList;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class ArrayDeclaration extends Statement implements INode {
    private final ArrayList<Value> values;
    private final ArrayVariable type;
    private int size;

    public ArrayDeclaration(ArrayVariable type){
        this.type = type;
        this.values = new ArrayList<>();
        this.size = 0;
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

    public ArrayVariable getType() {
        return type;
    }
}
