package compiler.Parser;

import java.util.ArrayList;

public class ProcDecl extends GeneralDecl {
    // Grammar : ProcDecl -> identifier ( Params ) Type Block
    public final ArrayList<Param> params;
    public final Block body;

    public ProcDecl(Type returnType, String identifier, ArrayList<Param> params, Block body) {
        super(returnType, identifier, null);
        this.params = params;
        this.body = body;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
        for (Param param : params) {
            param.accept(visitor);
        }
        body.accept(visitor);
    }
}
