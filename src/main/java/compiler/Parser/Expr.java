package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class Expr implements ASTNode{
    protected Type type; // Type of expression

    public Expr(String typeName) {
        this.type = new Type(typeName);
    }

    public String toString() {
        return "Expr{" +
                "type=" + type +
                '}';
    }
}

    public Type getType() {
        return this.type;
    }
    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);

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
