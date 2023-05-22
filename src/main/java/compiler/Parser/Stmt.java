package compiler.Parser;

import compiler.Exceptions.SemanticException;
import compiler.Lexer.Lexer;

import java.util.ArrayList;

public class Stmt implements ASTNode{
    protected final Lexer.Token name;
    public Stmt(Lexer.Token name) {
        this.name = name;
    }
    public Lexer.Token getName() {
        return name;

    public String toString() {
        return "Stmt{" +
                "name=" + name +
                '}';
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

    public String toString() {
        return "CtrlStruct{" +
                "name=" + name +
                ", condition=" + condition.toString() +
                ", body=" + body.toString() +
                '}';
    }
}

    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }

    public String toString() {
        BinaryExpr cnd = (BinaryExpr) condition;
        return "For{" +
                "name=" + name +
                ", init=" + cnd.left.toString() +
                ", end=" + cnd.right.toString() +
                ", step=" + step.toString() +
                ", body=" + body.toString() +
                '}';
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

    public String toString() {
        return "Assignment{" +
                "name=" + name +
                ", left=" + left.toString() +
                ", value=" + value.toString() +
                '}';
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
    protected final String field;

    public RecordAccess(Lexer.Token name, String identifier, String field) {
        super(name, identifier);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

