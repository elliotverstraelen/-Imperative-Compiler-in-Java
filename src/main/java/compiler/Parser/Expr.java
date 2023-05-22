package compiler.Parser;

import compiler.Lexer.Lexer;

import java.util.ArrayList;
import java.util.HashMap;

public class Expr {
    public final String type; // Type of expression

    public Expr(String type) {
        this.type = type;
    }

    public String toString() {
        return "Expr{" +
                "type=" + type +
                '}';
    }
}

class BinaryExpr extends Expr {
    public final Expr left; // Left expression
    public final Expr right; // Right expression
    public final Lexer.Token operator; // Operator

    public BinaryExpr(Expr left, Expr right, Lexer.Token operator) {
        super("BinaryExpr");
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public String toString() {
        return "{" +
                (left == null ? null : left.toString()) +
                operator +
                (right == null ? null : right.toString()) +
                '}';
    }
}

class IntegerExpr extends Expr {
    public final int value; // Value of the integer

    public IntegerExpr(int value) {
        super("IntegerExpr");
        this.value = value;
    }

    public String toString() {
        return "{" + value + '}';
    }
}

class RealExpr extends Expr {
    public final double value; // Value of the real

    public RealExpr(double value) {
        super("RealExpr");
        this.value = value;
    }

    public String toString() {
        return "{" + value + '}';
    }
}

class BooleanExpr extends Expr {
    public final boolean value; // Value of the boolean

    public BooleanExpr(boolean value) {
        super("BooleanExpr");
        this.value = value;
    }

    public String toString() {
        return "{" + value + '}';
    }
}

class StringExpr extends Expr {
    public final String value; // Value of the string

    public StringExpr(String value) {
        super("StringExpr");
        this.value = value;
    }

    public String toString() {
        return "{" + value + '}';
    }
}

class IdentifierExpr extends Expr {
    public final String identifier; // Identifier of the variable

    public IdentifierExpr(String identifier) {
        super("IdentifierExpr");
        this.identifier = identifier;
    }

    public String toString() {
        return "{" + identifier + '}';
    }
}

class ArrayExpr extends Expr {
    public final Type type; // Type of the elements of the array
    public final ArrayList<Expr> content; // Content of the array

    public ArrayExpr(Type type, ArrayList<Expr> content) {
        super("ArrayExpr");
        this.type = type;
        this.content = content;
    }

    public String toString() {
        return "{" + type.name + "[" + content.toString() + "]}";
    }
}

class RecordExpr extends Expr {
    public ArrayList<RecordEntry> content; // Content of the record

    public RecordExpr(String type, ArrayList<RecordEntry> content) {
        super(type);
        this.content = content;
    }

    public String toString() {
        return "{" + content.toString() + '}';
    }
}
