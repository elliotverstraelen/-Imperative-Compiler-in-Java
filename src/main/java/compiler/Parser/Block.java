package compiler.Parser;

import java.util.ArrayList;

class Block {
    private final ArrayList<Stmt> statements;
    public Block(ArrayList<Stmt> statements) {
        this.statements = statements;
    }
}
