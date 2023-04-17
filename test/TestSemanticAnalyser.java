import compiler.Exceptions.SemanticException;
import compiler.Lexer.*;
import compiler.Parser.*;
import compiler.SemanticAnalyser.*;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class TestSemanticAnalyser {

    /**
     * Basic example
     */
    @Test
    public void testBasic() {
        String input = "var x int = 2; var y int = ((3 + 4) * 5);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        try {
            program.accept(semanticAnalyser);
        } catch (SemanticException e) {
            // Should not crash here
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test if a variable is used without being declared
     */
    @Test
    public void testNotDeclared() {
        String input = "x = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        });
    }

    /**
     * Test if a variable is declared twice
     */
    @Test
    public void testDoubleDeclaration() {
        String input = "var x int = 2; var x int = 3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        });
    }

    /**
     * Test wrong type assignment
     */
    @Test
    public void testWrongTypeAssignment() {
        String input = "var x int = 2; x = true;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        });
    }

    /**
     * Test when declaring the same variable multiple times
     */
    @Test
    public void testMultipleVariableDeclarations() {
        String input = "var x int = 2; var x int = 3; var x int = 4;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        });
    }

    /**
     * More advanced example
     */
    @Test
    public void testAdvanced() {
        String input = "var x int = 2; var y int = ((3 + 4) * 5); var z string = \"Hello World\"; const wrong int = 8 + true;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            // Type mismatch : int + bool
            program.accept(semanticAnalyser);
        });
    }
}
