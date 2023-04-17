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

