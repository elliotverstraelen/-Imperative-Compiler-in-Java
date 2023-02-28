package compiler.Lexer;
import java.io.Reader;

public class Lexer {

    private Reader sourceFile;
    private int currentChar
    
    public Lexer(Reader input) {
        this.sourceFile = sourceFile;
        this.currentSymbol = NULL // Is this correct instanciation ?
    }

    public enum SymbolType {
        IDENTIFIER, //noms de variables, fonctions, types
        KEYWORD_CONST, KEYWORD_RECOR, KEYWORD_VAR, KEYWORD_VAL, KEYWORD_PROC, KEYWORD_FOR, KEYWORD_TO, KEYWORD_BY, KEYWORD_WHILE, KEYWORD_IF, KEYWORD_ELSE, KEYWORD_RETURN, KEYWORD_AND, KEYWORD_OR,
        INTEGER, // 32-bit
        REAL, // 64-bit
        STRING, // String
        BOOLEAN, // true or false
        SYMBOL_ASSIGN, SYMBOL_PLUS, SYMBOL_MINUS, SYMBOL_MULTIPLY, SYMBOL_DIVIDE, SYMBOL_MODULO, SYMBOL_EQUAL, SYMBOL_NOT_EQUAL, SYMBOL_LESS_THAN, SYMBOL_GREATER_THAN, SYMBOL_LESS_THAN_OR_EQUAL, SYMBOL_GREATER_THAN_OR_EQUAL, SYMBOL_LEFT_PARENTHESIS, SYMBOL_RIGHT_PARENTHESIS, SYMBOL_LEFT_BRACE, SYMBOL_RIGHT_BRACE, SYMBOL_LEFT_BRACKET, SYMBOL_RIGHT_BRACKET, SYMBOL_DOT, SYMBOL_SEMICOLON, SYMBOL_COMMA,
        MUTABILITY_VAR, MUTABILITY_VAL,
        EOF // end of file
    }

    private int readNextChar() throws IOException {
        try{
            return this.sourceFile.read();
        }catch(IOException e) {
            e.printStackTrace();
            return -1;

        }
    }

    public Symbol getNextSymbol() throws IOException {
        StringBuilder lexemeBuilder = new StringBuilder();
        int state = 0;

        // Skip Whitespace
        while(Character.isWhitespace(currentChar)){
            readNextChar();
        }

        // check if end of input
        if (currentChar == -1) {
            return new Symbol(SymbolType.EOF, null);
        }
        //check for comments

        //check for identifiers and keywords

        //check for values

        //check for strings

        //check for special symbols =+-*/%<>!(){}[].;,?
    }

}
