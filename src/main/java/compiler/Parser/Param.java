package compiler.Parser;

public class Param implements ASTNode {
    public final String name;
    public final Type type;

    public Param(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
