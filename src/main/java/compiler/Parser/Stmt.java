package compiler.Parser;

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

    public void accept(ASTVisitor visitor) {
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

class ArrayAccess extends Left {
    protected final Expr index;

    public ArrayAccess(Lexer.Token name, String identifier, Expr index) {
        super(name, identifier);
        this.index = index;
    }
    public Expr getIndex() {
        return index;
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

class Return extends Stmt {
    protected final Expr value;

    public Return(Expr value) {
        super(Lexer.Token.KEYWORD_RETURN);
        this.value = value;
    }
    public Expr getValue() {
        return value;
    }
}
