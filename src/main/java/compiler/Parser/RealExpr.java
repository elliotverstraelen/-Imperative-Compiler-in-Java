package compiler.Parser;

public class RealExpr extends Expr {
    protected final double value; // Value of the real

    public RealExpr(double value) {
        super("RealExpr");
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "{" + value + '}';
    }
}
