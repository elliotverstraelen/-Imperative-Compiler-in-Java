package compiler.Parser;

import java.util.ArrayList;

public class Proc {
    public final String identifier; // Name of the procedure
    public Proc(String identifier) {
        this.identifier = identifier;
    }
}
class ProcDecl extends Proc {
    // Grammar : ProcDecl -> identifier ( Params ) Type Block
    public final ArrayList<Param> params;
    public final Type returnType; // Return type of the procedure
    public final Block body;

    public ProcDecl(String identifier, ArrayList<Param> params, Type returnType, Block body) {
        super(identifier);
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }
}
