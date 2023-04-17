package compiler.Parser;

import compiler.SemanticAnalyser.SemanticException;

import java.util.ArrayList;

public class RecordT extends GeneralDecl{
    public final String identifier;
    public final ArrayList<RecordEntry> fields;

    public RecordT(String identifier, ArrayList<RecordEntry> fields) {
        // Assuming the RecordT has a null Type and Expr value
        super(null, identifier, null);
        this.identifier = identifier;
        this.fields = fields;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (RecordEntry entry : fields) {
            entry.accept(visitor);
        }
    }
}

