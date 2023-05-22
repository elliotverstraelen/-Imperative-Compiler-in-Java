package compiler.Parser;

import compiler.Lexer.Lexer;

public class Assignment extends Stmt {
    protected final Object left; // left part of the assignment
    protected final Expr value; // right part of the assignment

    public Assignment(Lexer.Token name, Object left, Expr value) {
        super(name);
        this.left = left;
        this.value = value;
    }

    public Object getLeft() {
        return left;
    }

    public Expr getValue() {
        return value;
    }
}
