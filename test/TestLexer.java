import compiler.Lexer.Symbol;
import org.junit.Test;

import java.io.StringReader;
import compiler.Lexer.Lexer;

import static compiler.Lexer.Lexer.SymbolType.*;
import static org.junit.Assert.*;

public class TestLexer {

    /**
     * Test if the lexer does not return null for the first symbol on a correct input
     */
    @Test
    public void testNotNull() {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertNotNull(lexer.getNextSymbol());
    }

    /**
     * Test if the lexer ignores comments
     */
    @Test
    public void testComments() {
        String input = "//This is a comment\nvar x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(lexer.currentSymbol.getLexeme(), "var");
    }

    /**
     * Test if the lexer correctly identifies the symbols in the input
     */
    @Test
    public void testSymbols() {
        String input = "var x bool = true; val y int = 2; val x real = 2.0; val z string = \"Hello World\";";
        Symbol[] expected = {new Symbol(KEYWORD_VAR, "var"), new Symbol(IDENTIFIER, "x"),
                new Symbol(IDENTIFIER, "bool"), new Symbol(SYMBOL_ASSIGN, "="),
                new Symbol(BOOLEAN, "true"), new Symbol(SYMBOL_SEMICOLON, ";"), new Symbol(KEYWORD_VAL, "val"),
                new Symbol(IDENTIFIER, "y"), new Symbol(IDENTIFIER, "int"), new Symbol(SYMBOL_ASSIGN, "="),
                new Symbol(INTEGER, "2"), new Symbol(SYMBOL_SEMICOLON, ";"), new Symbol(KEYWORD_VAL, "val"),
                new Symbol(IDENTIFIER, "x"), new Symbol(IDENTIFIER, "real"), new Symbol(SYMBOL_ASSIGN, "="),
                new Symbol(REAL, "2.0"), new Symbol(SYMBOL_SEMICOLON, ";"), new Symbol(KEYWORD_VAL, "val"),
                new Symbol(IDENTIFIER, "z"), new Symbol(IDENTIFIER, "string"), new Symbol(SYMBOL_ASSIGN, "="),
                new Symbol(STRING, "Hello World"), new Symbol(SYMBOL_SEMICOLON, ";"), new Symbol(EOF, null)};
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbol symbol = lexer.currentSymbol;
        for (Symbol expectedSymbol : expected) {
            // This will fail if the lexer returns a symbol or lexeme that is not in the expected array
            assertEquals(symbol.getSymbolType(), expectedSymbol.getSymbolType());
            assertEquals(symbol.getLexeme(), expectedSymbol.getLexeme());
            symbol = lexer.getNextSymbol();
        }
    }

    /**
     * Simple test to check if the lexer returns the correct first symbol
     */
    @Test
    public void simpleTest() {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(lexer.currentSymbol.getLexeme(), "var");
    }

    /*
     * Test if the lexer correctly identifies the symbols in a more complex input (including comments and newlines)
     */
    @Test
    public void moreComplexTest() {
        String input = "var x int = 2;//This should not appear\n var y int = ((3 + 4) * 5);";
        String[] expected = {"var", "x", "int", "=", "2", ";", "var", "y", "int", "=", "(", "(", "3", "+", "4", ")", "*", "5", ")", ";"};
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Symbol symbol = lexer.currentSymbol;
        assertEquals(symbol.getLexeme(), expected[0]);
        int index = 1;
        while ((symbol = lexer.getNextSymbol()).getLexeme() != null) {
            // This will fail if the lexer returns a symbol that is not in the expected array
            // Only stops when the lexer returns a null symbol (EOF)
            assertEquals(symbol.getLexeme(), expected[index]);
            index++;
        }
    }
}
