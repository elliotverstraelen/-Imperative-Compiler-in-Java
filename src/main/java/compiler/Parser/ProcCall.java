package compiler.Parser;

import compiler.Lexer.Lexer;

import java.util.ArrayList;

public class ProcCall extends Stmt {
    // Grammar : ProcCall -> identifier ( Exprs )
    protected final String identifier;
    protected final ArrayList<Expr> args;

    public ProcCall(String identifier, ArrayList<Expr> args) {
        super(Lexer.Token.KEYWORD_PROC);
        this.identifier = identifier;
        this.args = args;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<Expr> getArgs() {
        return args;
    }
}
