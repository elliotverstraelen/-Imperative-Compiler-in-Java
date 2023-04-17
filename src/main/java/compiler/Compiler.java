package compiler;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.Parser;
import compiler.Parser.Program;
import compiler.SemanticAnalyser.SemanticAnalyzer;
import compiler.Exceptions.SemanticException;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Compiler {
    private static void printLexer(Lexer lexer, Symbol symbol) {
        // Print first symbol
        System.out.println("Symbol type: " + symbol.getToken() + ", Lexeme: " + symbol.getLexeme());
        while ((symbol = lexer.getNextSymbol()).getLexeme() != null) {
            // Print all symbols
            System.out.println("Symbol type: " + symbol.getToken() + ", Lexeme: " + symbol.getLexeme());
        }
        // Print EOF symbol
        System.out.println("Symbol type: " + symbol.getToken() + ", Lexeme: " + symbol.getLexeme());
    }
    public void lexer(String[] args) {
        System.out.println("Testing lexer...");
        // Basic example of how to use the lexer
        System.out.println("--Basic example--");
        String input = "var x int = 2; var y int = ((3 + 4) * 5)))";
        StringReader reader = new StringReader(input); // Create a reader for the input
        Lexer lexer = new Lexer(reader); // Create a lexer for the reader
        Symbol symbol = lexer.currentSymbol; // Get the first symbol
        printLexer(lexer, symbol); // Print all symbols

        // More advanced example of how to use the lexer
        System.out.println("--More advanced example--");
        input = "//This is a comment\nvar x int = 2;\nvar y int = 3;\nvar z int = x + y;";
        reader = new StringReader(input); // Create a reader for the input
        lexer = new Lexer(reader); // Create a lexer for the reader
        symbol = lexer.currentSymbol; // Get the first symbol
        printLexer(lexer, symbol); // Print all symbols

        // Example using "code_example.lang" file
        System.out.println("--Example using \"code_example.lang\" file--");
        Path filename = Path.of("code_example.lang");
        try {
            input = Files.readString(filename); // Read the file
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        reader = new StringReader(input); // Create a reader for the input
        lexer = new Lexer(reader); // Create a lexer for the reader
        symbol = lexer.currentSymbol; // Get the first symbol
        printLexer(lexer, symbol); // Print all symbols
    }

    public Program parseInput(String input) throws Parser.ParserException {
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
        parser.parseProgram();

        return parser.getProgram();
    }

    public void analyseProgram(Program program) {
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        System.out.println("Performing semantic analysis...");
        try {
            program.accept(semanticAnalyzer);
        } catch (SemanticException e) {
            System.out.println("Semantic analysis failed!");
            System.out.println(e.getMessage());
        }
        System.out.println("Semantic analysis completed successfully!");
    }

    public void parser() throws Parser.ParserException {
        System.out.println("Testing parser...");
        // Basic example of how to use the lexer
        System.out.println("--Basic example--");
        String input = "var x int = 2; var y int = ((3 + 4) * 5);";
        parseInput(input);

        // More advanced example of how to use the lexer
        System.out.println("--More advanced example--");
        input = "//This is a comment\nvar x int = 2;\nvar y int = 3;\nvar z int = x + y;";
        parseInput(input);

        // Example using "code_example.lang" file
        System.out.println("--Example using \"code_example.lang\" file--");
        Path filename = Path.of("code_example.lang");
        try {
            input = Files.readString(filename); // Read the file
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        parseInput(input);
    }

    public void semanticAnalysis() throws Parser.ParserException {
        System.out.println("Testing semantic Analysis...");
        // Basic example
        System.out.println("--Basic example--");
        String input = "var x int = 2; var y int = ((3 + 4) * 5);";
        analyseProgram(parseInput(input));

        // Basing (wrong) example
        System.out.println("--Basic (wrong) example--");
        input = "var x int = \"hello\"; var y bool = 10;";
        analyseProgram(parseInput(input));

        // More advanced example
        System.out.println("--More advanced example--");
        input = "//This is a comment\nvar x int = 2;\nvar y int = 3;\nvar z int = x + y;";
        analyseProgram(parseInput(input));

        // Example using "code_example.lang" file
        Path filename = Path.of("code_example.lang");
        System.out.println("--Example using \"code_example.lang\" file--");
        try {
            input = Files.readString(filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        analyseProgram(parseInput(input));
    }
    

    public static void main(String[] args) throws Parser.ParserException {
        Compiler compiler = new Compiler();
        compiler.lexer(args);
        compiler.parser();
        compiler.semanticAnalysis();
    }
}
