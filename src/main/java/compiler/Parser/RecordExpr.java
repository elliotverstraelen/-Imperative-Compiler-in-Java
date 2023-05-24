package compiler.Parser;

import compiler.Exceptions.SemanticException;
import java.util.ArrayList;

public class RecordExpr extends Expr {
    protected final ArrayList<RecordEntry> content; // Content of the record

    public RecordExpr(String type, ArrayList<RecordEntry> content) {
        super(type);
        this.content = content;
    }

    public ArrayList<RecordEntry> getContent() {
        return content;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (Expr e : content) {
            e.accept(visitor);
        }
    }

    public String toString() {
        return "{" + content.toString() + '}';
    }
}
