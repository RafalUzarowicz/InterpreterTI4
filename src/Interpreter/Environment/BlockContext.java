package Interpreter.Environment;

import Utilities.ProgramTree.Variables.*;

import java.util.LinkedHashMap;

public class BlockContext {
    private LinkedHashMap<String, Variable> variables;

    public BlockContext(){
        variables = new LinkedHashMap<>();
    }

    public void setVariable(String name, Variable variable){
        variables.put(name, variableCopy(variable));
    }

    public boolean contains(String name){
        return variables.get(name) != null;
    }

    public void setVariableValue(String name, String value){
        variables.get(name).setValue(value);
    }

    public Variable getVariable(String name){
        return variables.get(name);
    }

    public int getSize(){
        return variables.size();
    }

    public LinkedHashMap<String, Variable> getMap(){
        return variables;
    }

    public Variable variableCopy(Variable variable){
        if (ArrayVariable.class.equals(variable.getClass())) {
            return new ArrayVariable(variableCopy(((ArrayVariable) variable).getVariable()), ((ArrayVariable) variable).size());
        } else if (BoolVariable.class.equals(variable.getClass())) {
            return new BoolVariable(variable.getName());
        } else if (ColorVariable.class.equals(variable.getClass())) {
            return new ColorVariable(variable.getName());
        } else if (HexVariable.class.equals(variable.getClass())) {
            return new HexVariable(variable.getName());
        } else if (IntVariable.class.equals(variable.getClass())) {
            return new IntVariable(variable.getName());
        } else if (PlanetVariable.class.equals(variable.getClass())) {
            return new PlanetVariable(variable.getName());
        } else if (StringVariable.class.equals(variable.getClass())) {
            return new StringVariable(variable.getName());
        } else if (UnitVariable.class.equals(variable.getClass())) {
            return new UnitVariable(variable.getName());
        } else if (VarVariable.class.equals(variable.getClass())) {
            return new VarVariable(variable.getName());
        }
        return new Variable(variable.getName());
    }
}
