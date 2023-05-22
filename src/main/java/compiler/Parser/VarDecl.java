package compiler.Parser;
import static compiler.Lexer.Lexer.Token.KEYWORD_VAR;

public class VarDecl extends GeneralDecl {
    public VarDecl(Type type, String identifier, Expr value) { super(KEYWORD_VAR, type, identifier, value); }
}
