package compiler.Parser;
import static compiler.Lexer.Lexer.Token.KEYWORD_VAL;

public class ValDecl extends GeneralDecl {
    public ValDecl(Type type, String identifier, Expr value) {
        super(KEYWORD_VAL, type, identifier, value);
    }
}
