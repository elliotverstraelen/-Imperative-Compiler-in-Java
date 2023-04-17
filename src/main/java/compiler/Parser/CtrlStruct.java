package compiler.Parser;

import compiler.Lexer.Lexer;

public class CtrlStruct extends Stmt {
    protected final Expr condition;
    protected final Block body;

    public CtrlStruct(Lexer.Token name, Expr condition, Block body) {
        super(name);
        this.condition = condition;
        this.body = body;
    }

    public Expr getCondition() {
        return condition;
    }

    public Block getBody() {
        return body;
    }
}
