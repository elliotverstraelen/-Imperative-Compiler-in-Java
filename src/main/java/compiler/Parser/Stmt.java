package compiler.Parser;

import compiler.Exceptions.SemanticException;
import compiler.Lexer.Lexer;

import java.util.ArrayList;

public class Stmt implements ASTNode{
    protected final Lexer.Token name;
    public Stmt(Lexer.Token name) {
        this.name = name;
    }
    public Lexer.Token getName() {
        return name;
    }

    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}

class Left extends Stmt {
    protected final String identifier;

    public Left(Lexer.Token name, String identifier) {
        super(name);
        this.identifier = identifier;
    }
    public String getIdentifier() {
        return identifier;
    }
}

class RecordAccess extends Left {
    protected final String field;

    public RecordAccess(Lexer.Token name, String identifier, String field) {
        super(name, identifier);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

