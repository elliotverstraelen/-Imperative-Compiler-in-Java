package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class RecordAccess extends Expr {
    protected final Expr identifier; // Identifier of the record (i.e. 'a', 'p[0]')
    protected final Expr field; // Field of the record (i.e. 'x', 'y')

    public RecordAccess(Expr identifier, Expr field) {
        super("RecordAccess");
        // i.e. a.x, p[0].y
        this.identifier = identifier;
        this.field = field;
    }

    public Expr getIdentifier() { return identifier; }

    public Expr getField() { return field; }

    public String toString() {
        return identifier + "." + field;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}
