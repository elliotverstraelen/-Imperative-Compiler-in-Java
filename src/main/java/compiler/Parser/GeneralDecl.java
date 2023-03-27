package compiler.Parser;

import compiler.Lexer.Lexer;

public class GeneralDecl {
    public final Lexer.Token name; // Const, Record, Var or Val
    public final Type type; // Type of the variable
    public final String identifier; // Identifier of the variable

    public Expr value; // Value of the variable

    public GeneralDecl(Lexer.Token name, Type type, String identifier, Expr value) {
        this.name = name;
        this.type = type;
        this.identifier = identifier;
        this.value = value;
    }
}
