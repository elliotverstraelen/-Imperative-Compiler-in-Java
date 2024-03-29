package compiler.Parser;

public class IdentifierExpr extends Expr {
    protected final String identifier; // Identifier of the variable

    public IdentifierExpr(String identifier) {
        super("identifier");
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setType(Type newType) { type = newType; }

    public String toString() {
        return "{" + identifier + '}';
    }
}
