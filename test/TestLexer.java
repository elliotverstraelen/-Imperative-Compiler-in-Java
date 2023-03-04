import compiler.Lexer.Symbol;
import org.junit.Test;

import java.io.StringReader;
import compiler.Lexer.Lexer;

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
    public void simpleTest() {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(lexer.currentSymbol.getLexeme(), "var");
    }

    @Test
    public void testComments() {
        String input = "//This is a comment\nvar x int = 2;";
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
