package compiler.Parser;

import compiler.Exceptions.SemanticException;
import compiler.Lexer.Lexer;

import java.util.ArrayList;

public class RecordDecl extends GeneralDecl{
    protected final String name;
    protected final ArrayList<RecordEntry> fields;

    public RecordDecl(String identifier, ArrayList<RecordEntry> fields) {
        super(Lexer.Token.KEYWORD_RECORD, null, identifier, null);
        this.name = identifier;
        this.fields = fields;
    }

    public String getName() { return name; }

    public ArrayList<RecordEntry> getFields() { return fields; }

    public String toString() {
        return "RecordDecl{" +
                "name=" + name +
                ", fields=" + fields.toString() +
                '}';
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}
