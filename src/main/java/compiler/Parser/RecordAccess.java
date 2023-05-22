package compiler.Parser;

import compiler.Lexer.Lexer;

public class RecordAccess extends Left {
    public final String field;

    public RecordAccess(Lexer.Token name, String identifier, String field) {
        super(name, identifier);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public String toString() {
        return "RecordAccess{" +
                "name=" + name +
                ", identifier=" + identifier +
                ", field=" + field +
                '}';
    }
}
