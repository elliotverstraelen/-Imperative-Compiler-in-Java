package compiler.Parser;

public class Type {
    public final String name;

    public Type(String name) {
        this.name = name;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}