package compiler.Parser;

import java.util.ArrayList;

public class RecordT {
    public final String identifier;
    public final ArrayList<RecordEntry> fields;

    public RecordT(String identifier, ArrayList<RecordEntry> fields) {
        this.identifier = identifier;
        this.fields = fields;
    }

    public String toString() {
        return "RecordT{" +
                "identifier=" + identifier +
                ", fields=" + fields.toString() +
                '}';
    }
}

class RecordEntry {
    public final String identifier;
    public final String type;
    public Object value;

    public RecordEntry(String identifier, String type, Object value) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return "RecordEntry{" +
                "identifier=" + identifier +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
