package compiler.Parser;

import compiler.Exceptions.SemanticException;
import compiler.Lexer.Lexer;

public class BinaryExpr extends Expr {
    protected final Expr left; // Left expression
    protected final Expr right; // Right expression
    protected final Lexer.Token operator; // Operator

    public BinaryExpr(Expr left, Expr right, Lexer.Token operator) {
        super("BinaryExpr");
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    public Lexer.Token getOperator() {
        return operator;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        left.accept(visitor);
        right.accept(visitor);
    }
}
