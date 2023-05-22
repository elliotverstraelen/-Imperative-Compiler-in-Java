package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class Block {
    protected final ArrayList<Object> statements;
    public Block(ArrayList<Object> statements) {
        this.statements = statements;
    }

    public ArrayList<Object> getStatements() {
        return statements;
    }
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (Object stmt : statements) {
            ((ASTNode) stmt).accept(visitor);
        }
    }

    public String toString() {
        return "Block{" +
                "statements=" + statements.toString() +
                '}';
    }
}
