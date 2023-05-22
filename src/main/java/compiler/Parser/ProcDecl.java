package compiler.Parser;

import java.util.ArrayList;

public class ProcDecl {
    // Grammar : ProcDecl -> identifier ( Params ) Type Block
    public final String identifier; // Name of the procedure
    public final ArrayList<Param> params;
    public final Type returnType; // Return type of the procedure
    public final Block body;

    public ProcDecl(String identifier, ArrayList<Param> params, Type returnType, Block body) {
        this.identifier = identifier;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }

    public String toString() {
        return "ProcDecl{" +
                "identifier=" + identifier +
                ", params=" + params.toString() +
                ", returnType=" + returnType.name +
                ", body=" + body.toString() +
                '}';
    }
}
