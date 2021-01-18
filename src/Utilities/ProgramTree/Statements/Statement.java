package Utilities.ProgramTree.Statements;

import Utilities.Position;
import Utilities.ProgramTree.INode;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class Statement implements INode {
    private final Position position;

    public Statement(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
