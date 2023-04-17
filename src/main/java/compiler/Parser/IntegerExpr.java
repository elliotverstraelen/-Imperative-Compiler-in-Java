package compiler.Parser;

public class IntegerExpr extends Expr {
    protected final int value; // Value of the integer

    public IntegerExpr(int value) {
        super("IntegerExpr");
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
