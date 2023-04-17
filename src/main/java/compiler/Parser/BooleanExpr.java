package compiler.Parser;

public class BooleanExpr extends Expr {
    public final boolean value; // Value of the boolean

    public BooleanExpr(boolean value) {
        super("BooleanExpr");
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
