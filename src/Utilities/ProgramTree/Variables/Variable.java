package Utilities.ProgramTree.Variables;

import Utilities.ProgramTree.INode;

public class Variable implements INode {
    private String value;
    private String name;

    public Variable(String name){
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object clone() {
        Variable variable = null;
        try {
            variable = (Variable) super.clone();
        } catch (CloneNotSupportedException e) {
            variable = new Variable(
                    this.getName());
        }
        return variable;
    }
}
