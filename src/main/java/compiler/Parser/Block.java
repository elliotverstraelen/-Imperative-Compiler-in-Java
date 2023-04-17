package compiler.Parser;

import java.util.ArrayList;

public class Block {
    protected final ArrayList<Stmt> statements;
    public Block(ArrayList<Stmt> statements) {
        this.statements = statements;
    }

    public ArrayList<Stmt> getStatements() {
        return statements;
    }
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
        for (Stmt stmt : statements) {
            stmt.accept(visitor);
        }
    }
}
