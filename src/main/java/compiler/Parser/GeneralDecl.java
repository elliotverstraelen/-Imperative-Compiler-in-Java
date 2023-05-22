package compiler.Parser;

import compiler.Exceptions.SemanticException;

public abstract class GeneralDecl implements ASTNode {
    //public final Lexer.Token name; // Const, Record, Var or Val
    protected Type type; // Type of the variable
    protected String identifier; // Identifier of the variable
    protected Expr value; // Value of the variable

    public GeneralDecl(Type type, String identifier, Expr value) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return "GeneralDecl{" +
                "name=" + name +
                ", type=" + type.name +
                ", identifier=" + identifier +
                ", value=" + value +
                '}';
    }

    public String getIdentifier() {
        return identifier;
    }

    public Type getType() {
        return type;
    }

    public Expr getValue() {
        return value;
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}

