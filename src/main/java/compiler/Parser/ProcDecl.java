package compiler.Parser;

import compiler.Exceptions.SemanticException;
import java.util.ArrayList;
import static compiler.Lexer.Lexer.Token.KEYWORD_PROC;

public class ProcDecl extends GeneralDecl {
    // Grammar : ProcDecl -> identifier ( Params ) Type Block
    public final String identifier; // Name of the procedure
    public final ArrayList<Param> params;
    public final Type returnType; // Return type of the procedure
    public final Block body;

    public ProcDecl(String identifier, ArrayList<Param> params, Type returnType, Block body) {
        super(KEYWORD_PROC, returnType, identifier, null);
        this.identifier = identifier;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }

    public String toString() {
        return "ProcDecl{" +
                "identifier=" + identifier +
                ", params=" + params.toString() +
                ", returnType=" + returnType.getName() +
                ", body=" + body.toString() +
                '}';
    }

    public ArrayList<Param> getParams() {
        return params;

    }
    public Block getBody() { return body; }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (Param param : params) {
            param.accept(visitor);
        }
        body.accept(visitor);
    }
}