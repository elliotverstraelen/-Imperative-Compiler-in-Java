package compiler.Lexer;

public class Symbol {
    private final Lexer.Token Token; // Type of symbol
    private final String lexeme; // Lexeme of symbol

    /**
     * Constructor for Symbol
     * @param Token - Type of symbol
     * @param lexeme - Lexeme of symbol
     */
    public Symbol(Lexer.Token Token, String lexeme) {
        this.Token = Token; // Set symbol type
        this.lexeme = lexeme; // Set lexeme
    }

    /**
     * Get the type of symbol
     * @return Token - Type of symbol
     */
    public Lexer.Token getToken(){
        return this.Token;
    }

    /**
     * Get the lexeme of symbol
     * @return lexeme - Lexeme of symbol
     */
    public String getLexeme() {
        return this.lexeme;
    }
}
