package compiler;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.Parser;
import compiler.Parser.Program;
import compiler.SemanticAnalyser.SemanticAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Compiler {
    private static void printLexer(Lexer lexer, Symbol symbol) {
        // Print first symbolfe
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

    public void semanticAnalysis(Program program) {
        System.out.println("Performing semantic analysis...");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        program.accept(semanticAnalyzer);
    }
    

    public static void main(String[] args) throws Parser.ParserException {
        Compiler compiler = new Compiler();
        compiler.lexer(args);
        compiler.parser();

        // Basic example
        String input = "var x int = 2; var y int = ((3 + 4) * 5);";
        Program programBasicExample = compiler.parseInput(input);

        // More advanced example
        input = "//This is a comment\nvar x int = 2;\nvar y int = 3;\nvar z int = x + y;";
        Program programAdvancedExample = compiler.parseInput(input);

        // Example using "code_example.lang" file
        Path filename = Path.of("code_example.lang");
        try {
            input = Files.readString(filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Program programFileExample = compiler.parseInput(input);

        // After parsing, perform semantic analysis
        // Choose the program you want to analyze, for example, programFileExample
        compiler.semanticAnalysis(programFileExample);
    }
}
