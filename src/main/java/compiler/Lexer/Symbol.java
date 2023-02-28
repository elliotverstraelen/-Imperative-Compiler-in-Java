package compiler.Lexer;

public class Symbol {
    private TokenType tokenType;
    private String lexem;

    public Symbol(TokenType tokenType, String lexeme) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }

    public tokenType getTokenType(){
        return this.tokenType;
    }
    public String getLexeme() {
        return this.lexeme;
    }
}
