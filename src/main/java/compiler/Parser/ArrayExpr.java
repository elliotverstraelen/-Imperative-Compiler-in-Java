package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class ArrayExpr extends Expr {
    protected final Type type; // Type of the elements of the array
    protected final Expr size; // Size of the array
    protected final ArrayList<Expr> content; // Content of the array

    public ArrayExpr(Type type, Expr size, ArrayList<Expr> content) {
        super(type.getName() + "[]");
        this.type = type;
        this.size = size;
        this.content = content;
    }

    public Expr getSize() { return size; }

    public ArrayList<Expr> getContent() {
        return content;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (Expr e : content) {
            e.accept(visitor);
        }
    }

    public String toString() {
        return type.getName() + content.toString() + "(" + size + ")";
    }
}
