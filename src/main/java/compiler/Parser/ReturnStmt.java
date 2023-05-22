package compiler.Parser;

import compiler.Lexer.Lexer;

public class ReturnStmt extends Stmt {
    protected final Expr value;

    public ReturnStmt(Expr value) {
        super(Lexer.Token.KEYWORD_RETURN);
        this.value = value;
    }

    public Expr getValue() {
        return value;
    }

    public String toString() {
        return "ReturnStmt{" +
                "value=" + value +
                '}';
    }
}
