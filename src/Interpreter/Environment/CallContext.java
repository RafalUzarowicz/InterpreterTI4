package Interpreter.Environment;

import Utilities.ProgramTree.Variables.Variable;

import java.util.LinkedList;

public class CallContext {
    private LinkedList<BlockContext> blockContexts;

    public CallContext(){
        blockContexts = new LinkedList<>();
    }

    public void pushBlockContext(){
        blockContexts.push(new BlockContext());
    }

    public BlockContext peekBlockContext(){
        return blockContexts.peek();
    }

    public void popBlockContext(){
        blockContexts.pop();
    }

    public Variable getVariable(String name){
        Variable variable;
        for (var context: blockContexts ) {
            variable = context.getVariable(name);
            if(variable != null){
                return variable;
            }
        }
        return null;
    }
}
