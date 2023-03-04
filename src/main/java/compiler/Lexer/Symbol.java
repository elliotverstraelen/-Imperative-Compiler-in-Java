package compiler.Lexer;

public class Symbol {
    private final Lexer.SymbolType SymbolType; // Type of symbol
    private final String lexeme; // Lexeme of symbol

    /**
     * Constructor for Symbol
     * @param SymbolType - Type of symbol
     * @param lexeme - Lexeme of symbol
     */
    public Symbol(Lexer.SymbolType SymbolType, String lexeme) {
        this.SymbolType = SymbolType; // Set symbol type
        this.lexeme = lexeme; // Set lexeme
    }

    /**
     * Get the type of symbol
     * @return SymbolType - Type of symbol
     */
    public Lexer.SymbolType getSymbolType(){
        return this.SymbolType;
    }

    /**
     * Get the lexeme of symbol
     * @return lexeme - Lexeme of symbol
     */
    public String getLexeme() {
        return this.lexeme;
    }
}
