package compiler.Parser;
import static compiler.Lexer.Lexer.Token.*;

public class Assignment extends GeneralDecl {
    public Assignment(Type type, String identifier, Expr value) {
        super(IDENTIFIER, type, identifier, value);
    }
}
