package compiler.Parser;

import compiler.Lexer.Lexer;

public class ArrayAccess extends Left {
    protected final Expr index;

    public ArrayAccess(Lexer.Token name, String identifier, Expr index) {
        super(name, identifier);
        this.index = index;
    }

    public Expr getIndex() {
        return index;
    }


}
