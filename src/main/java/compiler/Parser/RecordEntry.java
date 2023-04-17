package compiler.Parser;

import compiler.SemanticAnalyser.SemanticException;

public class RecordEntry implements ASTNode {
    protected final String identifier;
    protected final Type type;
    protected Object value;

    public RecordEntry(String identifier, String type, Object value) {
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

    public Object getValue() {
        return value;
    }
    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}
