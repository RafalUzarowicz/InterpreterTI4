package Utilities.ProgramTree;

import java.util.HashMap;

public class Program implements INode{
    private final HashMap<String, Function> functions;
    public Program(HashMap<String, Function> functions){
        this.functions = functions;
    }

    public Function getFunction(String name){
        return functions.get(name);
    }
}
