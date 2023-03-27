package compiler.Parser;

import java.util.ArrayList;

public class RecordT {
    private final String identifier;
    private final ArrayList<Param> fields;

    public RecordT(String identifier, ArrayList<Param> fields) {
        this.identifier = identifier;
        this.fields = fields;
    }
}
