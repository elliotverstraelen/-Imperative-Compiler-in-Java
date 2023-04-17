package compiler.Parser;

import compiler.SemanticAnalyser.SemanticException;

public class RecordEntry implements ASTNode {
    public final String identifier;
    public final Type type;
    public Object value;

    public RecordEntry(String identifier, String type, Object value) {
        this.identifier = identifier;
        this.type = new Type(type);
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}
