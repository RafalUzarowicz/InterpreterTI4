package Interpreter.Environment;

import Utilities.ProgramTree.Statements.Statement;
import Utilities.ProgramTree.Value.Literals.Literal;

import java.util.Stack;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Environment {
    private final Stack<CallContext> callContexts;
    private final Stack<Literal> values;
    private boolean hasReturned;
    private boolean hasContinued;
    private boolean hasBroken;
    private Statement currentStatement;

    public Environment() {
        callContexts = new Stack<>();
        values = new Stack<>();
        hasReturned = false;
        hasContinued = false;
        hasBroken = false;
        currentStatement = null;
    }

    public void pushCallContext() {
        callContexts.push(new CallContext());
    }

    public CallContext peekCallContext() {
        return callContexts.peek();
    }

    public void popCallContext() {
        callContexts.pop();
    }

    public void pushValue(Literal literal) {
        values.push(literal);
    }

    public Literal peekValue() {
        return values.peek();
    }

    public Literal popValue() {
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

    public int getCurrentStatementLine() {
        return currentStatement.getPosition().getLine();
    }

    public void setCurrentStatement(Statement currentStatement) {
        this.currentStatement = currentStatement;
    }
}
