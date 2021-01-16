package Utilities.ProgramTree;

import java.util.HashMap;
/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Program implements INode{
    private final HashMap<String, Function> functions;
    public Program(HashMap<String, Function> functions){
        this.functions = functions;
    }

    public Function getFunction(String name){
        return functions.get(name);
    }
}
