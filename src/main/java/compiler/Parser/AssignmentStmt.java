package compiler.Parser;

import compiler.Lexer.Lexer;

public class AssignmentStmt extends Stmt {
    protected final String identifier;
    protected final Expr value;

    public AssignmentStmt(String identifier, Expr value) {
        super(Lexer.Token.SYMBOL_ASSIGN);
        this.identifier = identifier;
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expr getValue() {
        return value;
    }
}
