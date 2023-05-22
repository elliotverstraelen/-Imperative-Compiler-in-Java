package compiler.Parser;
import compiler.Exceptions.SemanticException;

public class Expr implements ASTNode{
    protected Type type; // Type of expression

    public Expr(String typeName) {
        this.type = new Type(typeName);
    }

    public String toString() {
        return type.getName();
    }

    public Type getType() {
        return type;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}
