package compiler.Parser;

import java.util.ArrayList;

public class RecordT {
    public final String identifier;
    public final ArrayList<RecordEntry> fields;

    public RecordT(String identifier, ArrayList<RecordEntry> fields) {
        this.identifier = identifier;
        this.fields = fields;
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
}
