package compiler.Parser;

import compiler.Lexer.Lexer;

public class Left extends Stmt {
    protected final String identifier;

    public Left(Lexer.Token name, String identifier) {
        super(name);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
