package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class ProcDecl extends GeneralDecl {
    // Grammar : ProcDecl -> identifier ( Params ) Type Block
    protected final ArrayList<Param> params;
    protected final Block body;

    public ProcDecl(Type returnType, String identifier, ArrayList<Param> params, Block body) {
        super(returnType, identifier, null);
        this.params = params;
        this.body = body;
    }

    public ArrayList<Param> getParams() {
        return params;
    }

    public Block getBody() {
        return body;
    }
    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (Param param : params) {
            param.accept(visitor);
        }
        body.accept(visitor);
    }
}
