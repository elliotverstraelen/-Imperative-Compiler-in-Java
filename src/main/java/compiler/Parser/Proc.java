package compiler.Parser;

import java.util.ArrayList;

public class Proc {
    private final String identifier; // Name of the procedure
    public Proc(String identifier) {
        this.identifier = identifier;
    }
}
class ProcDecl extends Proc {
    // Grammar : ProcDecl -> identifier ( Params ) Type Block
    private final ArrayList<Param> params;
    private final Type returnType; // Return type of the procedure
    private final Block body;

    public ProcDecl(String identifier, ArrayList<Param> params, Type returnType, Block body) {
        super(identifier);
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }
}
