package Utilities.ProgramTree.Value;

import Utilities.ProgramTree.INode;

public class VariableValue extends Value implements INode {
    private String name;
    private int index;
    public VariableValue(String name, int index){
        this.name = name;
        this.index = index;
    }
    public VariableValue(String name){
        this.name = name;
        this.index = -1;
    }

    @Override
    public String toString() {
        if(index < 0){
            return name;
        }else{
            return name+"["+index+"]";
        }
    }
}
