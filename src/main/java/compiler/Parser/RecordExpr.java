package compiler.Parser;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordExpr extends Expr {
    protected final ArrayList<RecordEntry> content; // Content of the record

    public RecordExpr(String type, ArrayList<RecordEntry> content) {
        super(type);
        this.content = content;
    }

    public ArrayList<RecordEntry> getContent() {
        return content;
    }

    public String toString() {
        return "{" + content.toString() + '}';
    }
}
