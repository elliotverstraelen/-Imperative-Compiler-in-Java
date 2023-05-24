package compiler.CodeGenerator;

import compiler.Lexer.Lexer;

public abstract class CtrlStructCodeGenerator extends StmtCodeGenerator {
    protected final ExpressionCodeGenerator condition;
    protected final BlockCodeGenerator thenBody;
    protected final BlockCodeGenerator elseBody;

    public CtrlStructCodeGenerator(Lexer.Token name, ExpressionCodeGenerator condition, BlockCodeGenerator body) {
        super(name);
        this.condition = condition;
        this.thenBody = body;
        this.elseBody = null;
    }
    public CtrlStructCodeGenerator(Lexer.Token name, ExpressionCodeGenerator condition, BlockCodeGenerator thenBody, BlockCodeGenerator elseBody) {
        super(name);
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    public ExpressionCodeGenerator getCondition() {
        return condition;
    }

    public BlockCodeGenerator getBody() {
        return thenBody;
    }
    public BlockCodeGenerator getElseBody() {
        return elseBody;
    }
}
