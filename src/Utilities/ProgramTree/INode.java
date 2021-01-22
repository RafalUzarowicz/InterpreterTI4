package Utilities.ProgramTree;

import Interpreter.IVisitor;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 * Node interface for program tree and visitor schema.
 */
public interface INode {
    default void accept(IVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
