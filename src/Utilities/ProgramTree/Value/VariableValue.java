package Utilities.ProgramTree.Value;

import Utilities.ProgramTree.INode;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class VariableValue extends Value implements INode {
    private final String name;
    private final Value index;
    public VariableValue(String name, Value index){
        this.name = name;
        this.index = index;
    }
    public VariableValue(String name){
        this.name = name;
        this.index = null;
    }

    public String getName() {
        return name;
    }

    public Value getIndex() {
        return index;
    }

    @Override
    public String toString() {
        if(index == null){
            return name;
        }else{
            return name+"["+index+"]";
        }
    }
}
