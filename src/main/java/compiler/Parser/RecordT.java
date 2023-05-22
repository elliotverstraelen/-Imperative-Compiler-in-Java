package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class RecordT implements ASTNode {
    private final String identifier;
    private final ArrayList<RecordEntry> fields;

    public RecordT(String identifier, ArrayList<RecordEntry> fields) {
        this.identifier = identifier;
        this.fields = fields;
    }
    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<RecordEntry> getFields() {
        return fields;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (RecordEntry entry : fields) {
            entry.accept(visitor);
        }
    }

    public String toString() {
        return "RecordT{" +
                "identifier=" + identifier +
                ", fields=" + fields.toString() +
                '}';
    }
}

