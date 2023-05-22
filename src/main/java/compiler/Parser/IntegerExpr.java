package compiler.Parser;

public class IntegerExpr extends Expr {
    protected final int value; // Value of the integer

    public IntegerExpr(int value) {
        super("int");
        this.value = value;
    }
    @Override
    public Type getType() {
        return super.getType();
    }


    public int getValue() {
        return value;
    }

}
