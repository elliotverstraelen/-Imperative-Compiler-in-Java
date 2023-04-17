package compiler.Parser;

import compiler.Lexer.Lexer;

import java.util.ArrayList;

public class Stmt implements ASTNode{
    protected final Lexer.Token name;
    public Stmt(Lexer.Token name) {
        this.name = name;
    }
    public Lexer.Token getName() {
        return name;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

class CtrlStruct extends Stmt {
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

class For extends CtrlStruct {
    protected final Expr step;

    public For(Lexer.Token name, Expr init, Expr end, Expr step, Block body) {
        // Condition represents the condition of the for loop as a binary expression
        super(name, new BinaryExpr(init, end, Lexer.Token.SYMBOL_EQUAL), body);
        this.step = step;
    }
    public Expr getStep() {
        return step;
    }
}

class Assignment extends Stmt {
    protected final Object left; // left part of the assignment
    protected final Expr value; // right part of the assignment

    public Assignment(Lexer.Token name, Object left, Expr value) {
        super(name);
        this.left = left;
        this.value = value;
    }
    public Object getLeft() {
        return left;
    }

    public Expr getValue() {
        return value;
    }
}

class ProcCall extends Stmt {
    // Grammar : ProcCall -> identifier ( Exprs )
    protected final String identifier;
    protected final ArrayList<Expr> args;

    public ProcCall(String identifier, ArrayList<Expr> args) {
        super(Lexer.Token.KEYWORD_PROC);
        this.identifier = identifier;
        this.args = args;
    }
    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<Expr> getArgs() {
        return args;
    }
}

class Left extends Stmt {
    protected final String identifier;

    public Left(Lexer.Token name, String identifier) {
        super(name);
        this.identifier = identifier;
    }
    public String getIdentifier() {
        return identifier;
    }
}

class ArrayAccess extends Left {
    protected final Expr index;

    public ArrayAccess(Lexer.Token name, String identifier, Expr index) {
        super(name, identifier);
        this.index = index;
    }
    public Expr getIndex() {
        return index;
    }
}

class RecordAccess extends Left {
    protected final String field;

    public RecordAccess(Lexer.Token name, String identifier, String field) {
        super(name, identifier);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

class Return extends Stmt {
    protected final Expr value;

    public Return(Expr value) {
        super(Lexer.Token.KEYWORD_RETURN);
        this.value = value;
    }
    public Expr getValue() {
        return value;
    }
}
