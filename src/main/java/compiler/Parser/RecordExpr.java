package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class RecordExpr extends Expr {
    protected final ArrayList<RecordEntry> content; // Content of the record

    public RecordExpr(String type, ArrayList<RecordEntry> content) {
        super("record");
        this.content = content;
    }

    public ArrayList<RecordEntry> getContent() {
        return content;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (RecordEntry entry : content) {
            entry.accept(visitor);
        }
    }
}