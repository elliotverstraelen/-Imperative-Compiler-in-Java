package compiler.Parser;

import compiler.Exceptions.SemanticException;
import java.util.ArrayList;

public class ArrayAccessExpr extends Expr {
    protected final String identifier; // Identifier of the array (i.e. 'a')
    protected final Expr index; // Index of the array (i.e. '0')

    public ArrayAccessExpr(Type type, Expr index) {
        // i.e. a[0]
        super("ArrayAccessExpr");
        this.identifier = type.getName();
        this.index = index;
    }

    public String getIdentifier() { return identifier; }

    public Expr getIndex() { return index; }

    public String toString() {
        return identifier + "[" + index + "]";
    }
}
