package compiler.Parser;

import compiler.Exceptions.SemanticException;
import compiler.Lexer.Lexer;

public abstract class GeneralDecl implements ASTNode {
    public final Lexer.Token name; // Const, Record, Var, Proc
    protected Type type; // Type of the variable
    protected String identifier; // Identifier of the variable
    protected Expr value; // Value of the variable

    public GeneralDecl(Lexer.Token name, Type type, String identifier, Expr value) {
        this.name = name;
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return "GeneralDecl{" +
                "name=" + name +
                ", type=" + type.getName() +
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

