package compiler.Lexer;
import java.io.IOException;
import java.io.Reader;

public class Lexer {
    private final Reader sourceFile; // Source file
    private int currentChar; // Current character
    public Symbol currentSymbol; // Current symbol

    /**
     * Constructor
     * @param input Source file
     */
    public Lexer(Reader input) {
        // Initialize lexer
        this.sourceFile = input; // Set source file
        if (readNextChar() == -1) {
            // Error while reading next character
            this.currentSymbol = new Symbol(Token.EOF, null);
        }
        this.currentSymbol = getNextSymbol(); // Get first symbol
    }

    /**
     * Symbol type
     */
    public enum Token {
        // Token types
        IDENTIFIER, // Variable names, function names, type names
        KEYWORD_CONST, KEYWORD_RECORD, KEYWORD_VAR, KEYWORD_VAL, KEYWORD_PROC, KEYWORD_FOR, KEYWORD_TO, KEYWORD_BY, // Keywords
        KEYWORD_WHILE, KEYWORD_IF, KEYWORD_ELSE, KEYWORD_RETURN, KEYWORD_AND, KEYWORD_OR, // Logical operators
        INTEGER, // 32-bit
        REAL, // 64-bit
        STRING, // String
        BOOLEAN, // true or false
        VOID, // Void type
        SYMBOL_ASSIGN, SYMBOL_PLUS, SYMBOL_MINUS, SYMBOL_MULTIPLY, SYMBOL_DIVIDE, SYMBOL_MODULO, SYMBOL_EQUAL, SYMBOL_NOT_EQUAL, SYMBOL_LESS_THAN, SYMBOL_GREATER_THAN, SYMBOL_LESS_THAN_OR_EQUAL, SYMBOL_GREATER_THAN_OR_EQUAL, // Operators
        SYMBOL_LEFT_PARENTHESIS, SYMBOL_RIGHT_PARENTHESIS, SYMBOL_LEFT_BRACE, SYMBOL_RIGHT_BRACE, SYMBOL_LEFT_BRACKET, SYMBOL_RIGHT_BRACKET, SYMBOL_DOT, SYMBOL_COMMA, // Special symbols
        ARRAY, // Array type (for parser)
        RECORD, // Record type (for parser)
        SYMBOL_SEMICOLON, // End of statement
        EOF, // End of file
        UNKNOWN // Unknown symbol
    }

    /**
     * Read next character from source file
     * @return 0 if successful, -1 if error
     */
    private int readNextChar() {
        try {
            this.currentChar = this.sourceFile.read();
            return 0;
        } catch(IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Get next symbol from source file
     * @return Next symbol from source file
     */
    public Symbol getNextSymbol() {
        StringBuilder lexemeBuilder = new StringBuilder(); // String builder for lexeme

        // Skip Whitespace
        while(Character.isWhitespace(currentChar)){
            if (readNextChar() == -1) {
                // Error while reading next character
                return new Symbol(Token.EOF, null);
            }
        }

        // Check if end of input
        if (currentChar == -1) {
            return new Symbol(Token.EOF, null);
        }
        // Check for comments
        if (currentChar == '/') {
            while (currentChar != '\n' && currentChar != -1) {
                // Skip comment
                if (readNextChar() == -1) {
                    // Error while reading next character
                    return new Symbol(Token.EOF, null);
                }
            }
            // Skip newline
            if (readNextChar() == -1) {
                // Error while reading next character
                return new Symbol(Token.EOF, null);
            }
            return getNextSymbol(); // Get next symbol
        }

        // Check for identifiers, keywords and boolean values
        if (currentChar == '_' || Character.isLetter(currentChar)) {
            while (currentChar == '_' || Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
                // Add character to lexeme
                lexemeBuilder.append((char) currentChar);
                if (readNextChar() == -1) {
                    // Error while reading next character
                    return new Symbol(Token.EOF, null);
                }
            }
            // Check if lexeme is a keyword
            String lexeme = lexemeBuilder.toString();
            return switch (lexeme) {
                case "const" -> new Symbol(Token.KEYWORD_CONST, lexeme);
                case "record" -> new Symbol(Token.KEYWORD_RECORD, lexeme);
                case "var" -> new Symbol(Token.KEYWORD_VAR, lexeme);
                case "val" -> new Symbol(Token.KEYWORD_VAL, lexeme);
                case "proc" -> new Symbol(Token.KEYWORD_PROC, lexeme);
                case "for" -> new Symbol(Token.KEYWORD_FOR, lexeme);
                case "to" -> new Symbol(Token.KEYWORD_TO, lexeme);
                case "by" -> new Symbol(Token.KEYWORD_BY, lexeme);
                case "while" -> new Symbol(Token.KEYWORD_WHILE, lexeme);
                case "if" -> new Symbol(Token.KEYWORD_IF, lexeme);
                case "else" -> new Symbol(Token.KEYWORD_ELSE, lexeme);
                case "return" -> new Symbol(Token.KEYWORD_RETURN, lexeme);
                case "and" -> new Symbol(Token.KEYWORD_AND, lexeme);
                case "or" -> new Symbol(Token.KEYWORD_OR, lexeme);
                case "true", "false" -> new Symbol(Token.BOOLEAN, lexeme);
                case "void" -> new Symbol(Token.VOID, lexeme);
                default -> new Symbol(Token.IDENTIFIER, lexeme);
            };
        }

        // Check for values
        if (Character.isDigit(currentChar)) {
            while (Character.isDigit(currentChar)) {
                // Add character to lexeme
                lexemeBuilder.append((char) currentChar);
                if (readNextChar() == -1) {
                    // Error while reading next character
                    return new Symbol(Token.EOF, null);
                }
            }
            // Check if lexeme is a real or integer
            if (currentChar == '.') {
                // Lexeme is a real
                lexemeBuilder.append((char) currentChar);
                if (readNextChar() == -1) {
                    // Error while reading next character
                    return new Symbol(Token.EOF, null);
                }
                while (Character.isDigit(currentChar)) {
                    // Add character to lexeme
                    lexemeBuilder.append((char) currentChar);
                    if (readNextChar() == -1) {
                        // Error while reading next character
                        return new Symbol(Token.EOF, null);
                    }
                }
                return new Symbol(Token.REAL, lexemeBuilder.toString());
            } else {
                // Lexeme is an integer
                return new Symbol(Token.INTEGER, lexemeBuilder.toString());
            }
        }

        // Check for strings
        if (currentChar == '"') {
            if (readNextChar() == -1) {
                // Error while reading next character
                return new Symbol(Token.EOF, null);
            }
            while (currentChar != '"') {
                // Add character to lexeme
                lexemeBuilder.append((char) currentChar);
                if (readNextChar() == -1) {
                    // Error while reading next character
                    return new Symbol(Token.EOF, null);
                }
            }
            if (readNextChar() == -1) {
                // Error while reading next character
                return new Symbol(Token.EOF, null);
            }
            return new Symbol(Token.STRING, lexemeBuilder.toString());
        }

        // Check for special symbols =+-*/%<>!(){}[].;,?
        String specialSymbols = "=+-*/%<>!(){}[].;,?";
        if (specialSymbols.contains(Character.toString((char) currentChar))) {
            // Add character to lexeme
            lexemeBuilder.append((char) currentChar);
            // Avoid duplicate symbols (otherwise i.e. "((" would be tagged as unknown symbol)
            String duplicateSymbols = "(){}[]";
            boolean canBeDuplicateSymbol = duplicateSymbols.contains(Character.toString((char) currentChar));
            // Get next character
            if (readNextChar() == -1) {
                // Error while reading next character
                return new Symbol(Token.EOF, null);
            }
            // Avoid comments without space (otherwise i.e. ";/" would be tagged as unknown symbol)
            if (specialSymbols.contains(Character.toString((char) currentChar)) && !canBeDuplicateSymbol && currentChar != '/') {
                // Add character to lexeme
                lexemeBuilder.append((char) currentChar);
                if (readNextChar() == -1) {
                    // Error while reading next character
                    return new Symbol(Token.EOF, null);
                }
            }
            // Check if lexeme is a special symbol
            String lexeme = lexemeBuilder.toString();
            return switch (lexeme) {
                case "=" -> new Symbol(Token.SYMBOL_ASSIGN, lexeme);
                case "+" -> new Symbol(Token.SYMBOL_PLUS, lexeme);
                case "-" -> new Symbol(Token.SYMBOL_MINUS, lexeme);
                case "*" -> new Symbol(Token.SYMBOL_MULTIPLY, lexeme);
                case "/" -> new Symbol(Token.SYMBOL_DIVIDE, lexeme);
                case "%" -> new Symbol(Token.SYMBOL_MODULO, lexeme);
                case "==" -> new Symbol(Token.SYMBOL_EQUAL, lexeme);
                case "!=" -> new Symbol(Token.SYMBOL_NOT_EQUAL, lexeme);
                case "<" -> new Symbol(Token.SYMBOL_LESS_THAN, lexeme);
                case ">" -> new Symbol(Token.SYMBOL_GREATER_THAN, lexeme);
                case "<=" -> new Symbol(Token.SYMBOL_LESS_THAN_OR_EQUAL, lexeme);
                case ">=" -> new Symbol(Token.SYMBOL_GREATER_THAN_OR_EQUAL, lexeme);
                case "(" -> new Symbol(Token.SYMBOL_LEFT_PARENTHESIS, lexeme);
                case ")" -> new Symbol(Token.SYMBOL_RIGHT_PARENTHESIS, lexeme);
                case "{" -> new Symbol(Token.SYMBOL_LEFT_BRACE, lexeme);
                case "}" -> new Symbol(Token.SYMBOL_RIGHT_BRACE, lexeme);
                case "[" -> new Symbol(Token.SYMBOL_LEFT_BRACKET, lexeme);
                case "]" -> new Symbol(Token.SYMBOL_RIGHT_BRACKET, lexeme);
                case "." -> new Symbol(Token.SYMBOL_DOT, lexeme);
                case ";" -> new Symbol(Token.SYMBOL_SEMICOLON, lexeme);
                case "," -> new Symbol(Token.SYMBOL_COMMA, lexeme);
                default -> new Symbol(Token.UNKNOWN, lexeme);
            };
        }
        return new Symbol(Token.UNKNOWN, "Unknown symbol");
    }
}
