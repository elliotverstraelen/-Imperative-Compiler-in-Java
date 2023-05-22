package compiler.Parser;

import compiler.Lexer.Lexer;

public class For extends CtrlStruct {
    protected final Expr step;

    public For(Lexer.Token name, Expr init, Expr end, Expr step, Block body) {
        // Condition represents the condition of the for loop as a binary expression
        super(name, new BinaryExpr(init, end, Lexer.Token.SYMBOL_EQUAL), body);
        this.step = step;
    }

    public Expr getStep() {
        return step;
    }

    public String toString() {
        BinaryExpr cnd = (BinaryExpr) condition;
        return "For{" +
                "name=" + name +
                ", init=" + cnd.left.toString() +
                ", end=" + cnd.right.toString() +
                ", step=" + step.toString() +
                ", body=" + thenBody.toString() +
                '}';
    }
}
