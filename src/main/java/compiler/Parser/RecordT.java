package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class RecordT extends GeneralDecl{
    private final String identifier;
    private final ArrayList<RecordEntry> fields;

    public RecordT(String identifier, ArrayList<RecordEntry> fields) {
        // Assuming the RecordT has a null Type and Expr value
        super(null, identifier, null);
        this.identifier = identifier;
        this.fields = fields;
    }
    public String getIdentifier() {
        return identifier;
    }

    public String toString() {
        return "RecordT{" +
                "identifier=" + identifier +
                ", fields=" + fields.toString() +
                '}';
    }
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
        return "RecordEntry{" +
                "identifier=" + identifier +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}

