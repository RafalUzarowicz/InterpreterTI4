package Utilities.ProgramTree.Variables;

import Utilities.ProgramTree.INode;
import Utilities.ProgramTree.Value.Literals.Literal;

import java.util.ArrayList;

public class ArrayVariable extends Variable implements INode {
    private ArrayList<String> values;
    private Variable variable;


    public ArrayVariable(Variable variable, int size){
        super(variable.getName());
        this.variable = variable;
        values = new ArrayList<>();
        for(int i = 0; i<size; ++i){
            values.add("");
        }
    }

    public void setValue(int index, String value) {
        values.set(index, value);
    }

    public int size(){
        return values.size();
    }

    public Variable getVariable() {
        return variable;
    }
}
