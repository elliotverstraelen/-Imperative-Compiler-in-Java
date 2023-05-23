package compiler.Parser;

public class ArrayAssignment extends Assignment {
    private final Expr index;
    public ArrayAssignment(Type type, String identifier, Expr index, Expr value) {
        super(type, identifier, value);
        this.index = index;
    }

    public Expr getIndex() { return index; }

    public String toString() {
        return "ArrayAssignment{" + type + ":" + identifier + "[" + index + "]=" + value + "}";
    }
}
