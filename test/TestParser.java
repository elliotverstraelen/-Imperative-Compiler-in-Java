import compiler.Lexer.Lexer;
import compiler.Parser.Parser;
import compiler.Parser.Program;
import compiler.Parser.VarDecl;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class TestParser {

    /**
     * Test if the Parser does not return null for the first symbol on a correct input
     */
    @Test
    public void testNotNull() {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        assertNotNull(parser.getProgram());
        assertNotNull(parser.getProgram().getContent());
        assertNotNull(parser.getProgram().getContent().get(0));
    }

    /**
     * Test variable declaration
     */
    @Test
    public void testVariableDeclaration() {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        Program program = parser.getProgram();
        assertEquals(program.getContent().size(), 1);
        assertEquals(program.getContent().get(0).getClass().getSimpleName(), "VarDecl");
    }

    /**
     * Test variable assignment
     */
    @Test
    public void testVariableAssignment() {
        String input = "var x int = 2; x = 10;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        Program program = parser.getProgram();
        assertEquals(program.getContent().size(), 2);
        assertEquals(program.getContent().get(0).getClass().getSimpleName(), "VarDecl");
        assertEquals(program.getContent().get(1).getClass().getSimpleName(), "Assignment");
    }

    /**
     * Test that the parser throws an exception when a variable incorrectly declared
     */
    @Test
    public void testIncorrectVariableDeclaration() {
        String input = "var x = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertThrows(RuntimeException.class, () -> {
            Parser parser = new Parser(lexer);
        });
    }

    /**
     * Procedure declaration
     */
    @Test
    public void testProcedureDeclaration() {
        String input = "proc main() void { var x int = 2; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        Program program = parser.getProgram();
        assertEquals(program.getContent().size(), 1);
        assertEquals(program.getContent().get(0).getClass().getSimpleName(), "ProcDecl");
    }

    /**
     * Basic example of a program
     */
    @Test
    public void testBasicProgram() {
        String input = "var x int = 2; var y int = ((3 + 4) * 5);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        Program program = parser.getProgram();
        assertEquals(program.getContent().size(), 2);
        assertEquals(program.getContent().get(0).getClass().getSimpleName(), "VarDecl");
        assertEquals(program.getContent().get(1).getClass().getSimpleName(), "VarDecl");
        assertEquals(((VarDecl) program.getContent().get(1)).getValue().getType().getName(), "BinaryExpr");
    }

    /**
     * More advanced example
     */
    @Test
    public void testAdvancedProgram() {
        String input = "//This is a comment\nvar x int = 2;\nvar y int = 3;\nvar z int = x + y;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        Program program = parser.getProgram();
        assertEquals(program.getContent().size(), 3);
        assertEquals(program.getContent().get(0).getClass().getSimpleName(), "VarDecl");
        assertEquals(((VarDecl) program.getContent().get(0)).getValue().getType().getName(), "int");
        assertEquals(program.getContent().get(1).getClass().getSimpleName(), "VarDecl");
        assertEquals(((VarDecl) program.getContent().get(1)).getValue().getType().getName(), "int");
        assertEquals(program.getContent().get(2).getClass().getSimpleName(), "VarDecl");
        assertEquals(((VarDecl) program.getContent().get(2)).getValue().getType().getName(), "BinaryExpr");
    }
}
