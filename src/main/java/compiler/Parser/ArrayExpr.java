package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class ArrayExpr extends Expr {
    protected final Type type; // Type of the elements of the array
    protected final ArrayList<Expr> content; // Content of the array

    public ArrayExpr(Type type, ArrayList<Expr> content) {
        super("ArrayExpr");
        this.type = type;
        this.content = content;
    }
    @Override
    public Type getType() {
        return this.type;
    }


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
}
