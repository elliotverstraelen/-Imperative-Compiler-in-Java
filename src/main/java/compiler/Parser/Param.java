package compiler.Parser;

import compiler.SemanticAnalyser.SemanticException;

public class Param implements ASTNode {
    protected final String name;
    protected final Type type;

    public Param(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}
