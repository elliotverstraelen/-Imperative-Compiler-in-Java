package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class Expr implements ASTNode{
    protected final String type; // Type of expression

    public Expr(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }

    public String toString() { return type; }
}

