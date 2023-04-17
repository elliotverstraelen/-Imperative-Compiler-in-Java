package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class Expr implements ASTNode{
    protected Type type; // Type of expression

    public Expr(String typeName) {
        this.type = new Type(typeName);
    }

    public Type getType() {
        return this.type;
    }
    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }

    public String toString() { return type.toString(); }
}

