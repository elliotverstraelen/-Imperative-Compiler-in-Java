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
     * Test using an undeclared variable in an expression
     */
    @Test
    public void testUsingUndeclaredVariableInExpression() {
        String input = "var x int = 2; var y int = x + z;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        });
    }

    /**
     * Test using an undeclared variable in a conditional statement
     */
    @Test
    public void testUsingUndeclaredVariableInCondition() {
        /* not implemented yet
        String input = "if (x < 10) { var y int = 5; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        }); */
    }

    /**
     * Test wrong type in a conditional statement
     */
    @Test
    public void testWrongTypeInCondition() {
        /* not implemented yet
        String input = "var x int = 2; if (x) { var y int = 5; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        }); */
    }

    /**
     * Test wrong type in a procedure call
     */
    @Test
    public void testWrongTypeInProcedureCall() {
        /* not implemented yet
        String input = "proc foo(x int) { var y int = x + 2; } foo(true);";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        }); */
    }

    /**
     * Test using a variable before its declaration
     */
    @Test
    public void testVariableUseBeforeDeclaration() {
        String input = "x = 2; var x int = 3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        });
    }

    /**
     * Test correct handling of type promotion
     */
    @Test
    public void testTypePromotion() {
        String input = "var x int = 2; var y real = 3.5; var z real = x + y;";
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
     * Test Array index out of bounds error
     */
    @Test
    public void testArrayIndexOutOfBounds() {
        // TODO: fix parser for this test
        String input = "var x int[] = int[](5); x[6] = 3;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        });
    }

    /**
     * Test improper procedure return type
     */
    @Test
    public void testImproperProcedureReturnType() {
        String input = "proc foo() int { return true; }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        });
    }

    /**
     * Test undeclared function call
     */
    @Test
    public void testUndeclaredProcedureCall() {
        /* not implemented yet
        String input = "func foo() int { return bar(); }";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        }); */
    }

    /**
     * Test uninitialized variable
     */
    @Test
    public void testUninitializedVariable() {
        /* Not supported yet
        String input = "var x int; var y int = x;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Program program = new Parser(lexer).getProgram();
        SemanticAnalyzer semanticAnalyser = new SemanticAnalyzer();
        assertThrows(SemanticException.class, () -> {
            program.accept(semanticAnalyser);
        }); */
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
