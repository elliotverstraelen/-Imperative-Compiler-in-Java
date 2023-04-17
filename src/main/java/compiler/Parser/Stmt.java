package compiler.Parser;

import compiler.Lexer.Lexer;

import java.util.ArrayList;

public class Stmt implements ASTNode{
    public final Lexer.Token name;
    public Stmt(Lexer.Token name) {
        this.name = name;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

class CtrlStruct extends Stmt {
    public final Expr condition;
    public final Block body;

    public CtrlStruct(Lexer.Token name, Expr condition, Block body) {
        super(name);
        this.condition = condition;
        this.body = body;
    }
}

class For extends CtrlStruct {
    public final Expr step;

    public For(Lexer.Token name, Expr init, Expr end, Expr step, Block body) {
        // Condition represents the condition of the for loop as a binary expression
        super(name, new BinaryExpr(init, end, Lexer.Token.SYMBOL_EQUAL), body);
        this.step = step;
    }
}

class Assignment extends Stmt {
    public final Object left; // left part of the assignment
    public final Expr value; // right part of the assignment

    public Assignment(Lexer.Token name, Object left, Expr value) {
        super(name);
        this.left = left;
        this.value = value;
    }
}

class ProcCall extends Stmt {
    // Grammar : ProcCall -> identifier ( Exprs )
    public final String identifier;
    public final ArrayList<Expr> args;

    public ProcCall(String identifier, ArrayList<Expr> args) {
        super(Lexer.Token.KEYWORD_PROC);
        this.identifier = identifier;
        this.args = args;
    }
}

class Left extends Stmt {
    public final String identifier;

    public Left(Lexer.Token name, String identifier) {
        super(name);
        this.identifier = identifier;
    }
}

class ArrayAccess extends Left {
    public final Expr index;

    public ArrayAccess(Lexer.Token name, String identifier, Expr index) {
        super(name, identifier);
        this.index = index;
    }
}

class RecordAccess extends Left {
    public final String field;

    public RecordAccess(Lexer.Token name, String identifier, String field) {
        super(name, identifier);
        this.field = field;
    }
}

class Return extends Stmt {
    public final Expr value;

    public Return(Expr value) {
        super(Lexer.Token.KEYWORD_RETURN);
        this.value = value;
    }
}
