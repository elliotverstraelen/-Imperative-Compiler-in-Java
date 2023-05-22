package compiler.Parser;

import compiler.Lexer.Lexer;

public class CtrlStruct extends Stmt {
    protected final Expr condition;
    protected final Block thenBody;
    protected final Block elseBody;

    public CtrlStruct(Lexer.Token name, Expr condition, Block body) {
        super(name);
        this.condition = condition;
        this.thenBody = body;
        this.elseBody = null;
    }
    public CtrlStruct(Lexer.Token name, Expr condition, Block thenBody, Block elseBody) {
        super(name);
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    public Expr getCondition() {
        return condition;
    }

    public Block getBody() {
        return thenBody;
    }
    public Block getElseBody() {
        return elseBody;
    }

    public String toString() {
        return "CtrlStruct{" +
                "name=" + name +
                ", condition=" + condition.toString() +
                ", thenBody=" + thenBody.toString() +
                ", elseBody=" + (elseBody == null ? null : elseBody.toString()) +
                '}';
    }
}
