import compiler.Lexer.Symbol;
import org.junit.Test;

import java.io.StringReader;
import compiler.Lexer.Lexer;

import static compiler.Lexer.Lexer.SymbolType.*;
import static org.junit.Assert.*;

public class TestLexer {

    @Test
    public void testNotNull() {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertNotNull(lexer.getNextSymbol());
    }

    @Test
    public void testComments() {
        String input = "//This is a comment\nvar x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(lexer.currentSymbol.getLexeme(), "var");
    }

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
            assertEquals(symbol.getLexeme(), expectedSymbol.getLexeme());
            assertEquals(symbol.getSymbolType(), expectedSymbol.getSymbolType());
            symbol = lexer.getNextSymbol();
        }
    }

    @Test
    public void simpleTest() {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(lexer.currentSymbol.getLexeme(), "var");
    }

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
            assertEquals(symbol.getLexeme(), expected[index]);
            index++;
        }
    }
}
