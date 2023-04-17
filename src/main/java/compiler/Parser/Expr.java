package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class Expr implements ASTNode{
    protected final String type; // Type of expression

    public Expr(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}

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

public class IntegerExpr extends Expr {
    protected final int value; // Value of the integer

    public IntegerExpr(int value) {
        super("IntegerExpr");
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

public class RealExpr extends Expr {
    protected final double value; // Value of the real

    public RealExpr(double value) {
        super("RealExpr");
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}

public class BooleanExpr extends Expr {
    public final boolean value; // Value of the boolean

    public BooleanExpr(boolean value) {
        super("BooleanExpr");
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}

public class StringExpr extends Expr {
    protected final String value; // Value of the string

    public StringExpr(String value) {
        super("StringExpr");
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

public class IdentifierExpr extends Expr {
    protected final String identifier; // Identifier of the variable

    public IdentifierExpr(String identifier) {
        super("IdentifierExpr");
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}

public class ArrayExpr extends Expr {
    protected final Type type; // Type of the elements of the array
    protected final ArrayList<Expr> content; // Content of the array

    public ArrayExpr(Type type, ArrayList<Expr> content) {
        super("ArrayExpr");
        this.type = type;
        this.content = content;
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

public class RecordExpr extends Expr {
    protected final ArrayList<RecordEntry> content; // Content of the record

    public RecordExpr(String type, ArrayList<RecordEntry> content) {
        super(type);
        this.content = content;
    }

    public ArrayList<RecordEntry> getContent() {
        return content;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (RecordEntry entry : content) {
            entry.accept(visitor);
        }
    }
}
