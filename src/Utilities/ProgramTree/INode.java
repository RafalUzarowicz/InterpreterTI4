package Utilities.ProgramTree;

import Interpreter.IVisitor;

public interface INode {
    default void accept(IVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
