package compiler.Parser;

import compiler.Lexer.Lexer;

import java.util.ArrayList;
import java.util.HashMap;

public class Expr {
    private final String type; // Type of expression

    public Expr(String type) {
        this.type = type;
    }
}

class BinaryExpr extends Expr {
    private final Expr left; // Left expression
    private final Expr right; // Right expression
    private final Lexer.Token operator; // Operator

    public BinaryExpr(Expr left, Expr right, Lexer.Token operator) {
        super("BinaryExpr");
        this.left = left;
        this.right = right;
        this.operator = operator;
    }
}

class IntegerExpr extends Expr {
    private final int value; // Value of the integer

    public IntegerExpr(int value) {
        super("IntegerExpr");
        this.value = value;
    }
}

class RealExpr extends Expr {
    private final double value; // Value of the real

    public RealExpr(double value) {
        super("RealExpr");
        this.value = value;
    }
}

class BooleanExpr extends Expr {
    private final boolean value; // Value of the boolean

    public BooleanExpr(boolean value) {
        super("BooleanExpr");
        this.value = value;
    }
}

class StringExpr extends Expr {
    private final String value; // Value of the string

    public StringExpr(String value) {
        super("StringExpr");
        this.value = value;
    }
}

class IdentifierExpr extends Expr {
    private final String identifier; // Identifier of the variable

    public IdentifierExpr(String identifier) {
        super("IdentifierExpr");
        this.identifier = identifier;
    }
}

class ArrayExpr extends Expr {
    private final ArrayList<Expr> exprs; // Expressions describing the array

    public ArrayExpr(ArrayList<Expr> exprs) {
        super("ArrayExpr");
        this.exprs = exprs;
    }
}

class RecordExpr extends Expr {
    private final HashMap<String, Expr> fields; // Fields of the record

    public RecordExpr(HashMap<String, Expr> fields) {
        super("RecordExpr");
        this.fields = fields;
    }
}
