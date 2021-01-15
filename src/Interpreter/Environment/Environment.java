package Interpreter.Environment;

import Utilities.ProgramTree.Value.Literals.Literal;

import java.util.Stack;

public class Environment {
    private final Stack<CallContext> callContexts;
    private final Stack<Literal> values;
    private boolean hasReturned = false;
    private boolean hasContinued = false;
    private boolean hasBroken = false;

    public Environment(){
        callContexts = new Stack<>();
        values = new Stack<>();
    }

    public void pushCallContext(){
        callContexts.push(new CallContext());
    }

    public CallContext peekCallContext() {
        return callContexts.peek();
    }

    public void popCallContext(){
        callContexts.pop();
    }

    public void pushValue(Literal literal){
        values.push(literal);
    }

    public Literal peekValue(){
        return values.peek();
    }

    public Literal popValue(){
        return values.pop();
    }

    public boolean hasReturned() {
        return hasReturned;
    }

    public void setHasReturned(boolean hasReturned) {
        this.hasReturned = hasReturned;
    }

    public boolean hasContinued() {
        return hasContinued;
    }

    public void setHasContinued(boolean hasContinued) {
        this.hasContinued = hasContinued;
    }

    public boolean hasBroken() {
        return hasBroken;
    }

    public void setHasBroken(boolean hasBroken) {
        this.hasBroken = hasBroken;
    }
}
