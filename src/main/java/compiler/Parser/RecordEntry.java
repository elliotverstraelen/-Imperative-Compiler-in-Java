package compiler.Parser;

import compiler.SemanticAnalyser.SemanticException;

public class RecordEntry implements ASTNode {
    public final String identifier;
    public final String type;
    public Object value;

    public RecordEntry(String identifier, String type, Object value) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}
