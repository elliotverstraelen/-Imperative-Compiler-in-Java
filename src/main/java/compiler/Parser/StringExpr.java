package compiler.Parser;

public class StringExpr extends Expr {
    protected final String value; // Value of the string

    public StringExpr(String value) {
        super("StringExpr");
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
