package compiler.Parser;

import compiler.Exceptions.SemanticException;

public class RecordEntry extends Expr {
    protected final String identifier;
    protected final Type type;
    protected Expr value;

    public RecordEntry(String identifier, String type, Expr value) {
        super("RecordEntry");
        this.identifier = identifier;
        this.type = new Type(type);
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Type getType() {
        return type;
    }

    public Expr getValue() {
        return value;
    }

    public String toString() {
        return "RecordEntry{" +
                "identifier=" + identifier +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
