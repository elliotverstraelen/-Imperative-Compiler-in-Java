package compiler.Parser;
import static compiler.Lexer.Lexer.Token.KEYWORD_CONST;

public class ConstDecl extends GeneralDecl {
    public ConstDecl(Type type, String identifier, Expr value) {
        super(KEYWORD_CONST, type, identifier, value);
    }
}
