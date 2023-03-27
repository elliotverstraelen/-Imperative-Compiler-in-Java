package compiler.Parser;

import java.util.ArrayList;

class Block {
    public final ArrayList<Stmt> statements;
    public Block(ArrayList<Stmt> statements) {
        this.statements = statements;
    }
}
