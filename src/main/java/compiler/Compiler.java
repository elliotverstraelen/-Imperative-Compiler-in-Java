package compiler;

import compiler.CodeGenerator.CodeGenerator;
import compiler.CodeGenerator.ProgramCodeGenerator;
import compiler.Exceptions.CodeGenerationException;
import compiler.Exceptions.SemanticException;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.Parser;
import compiler.Parser.Program;
import compiler.SemanticAnalyser.SemanticAnalyzer;

import java.io.File;
import java.io.FileOutputStream;
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
        String input = "var x int = 2; var y int = ((3 + 4) * 5));";
        System.out.println("Input: " + input);
        StringReader reader = new StringReader(input); // Create a reader for the input
        Lexer lexer = new Lexer(reader); // Create a lexer for the reader
        Symbol symbol = lexer.currentSymbol; // Get the first symbol
        printLexer(lexer, symbol); // Print all symbols

        // More advanced example of how to use the lexer
        System.out.println("--More advanced example--");
        input = "//This is a comment\nvar x int = 2;\nvar y int = 3;\nvar z int = x + y;";
        System.out.println("Input: " + input);
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

        return parser.getProgram();
    }

    public void analyseProgram(Program program) {
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        System.out.println("Performing semantic analysis...");
        try {
            program.accept(semanticAnalyzer);
            System.out.println("Semantic analysis completed successfully!");
        } catch (SemanticException e) {
            System.out.println(e.getMessage());
            System.out.println("Semantic analysis failed!");
        }
    }

    public void parser() throws Parser.ParserException {
        System.out.println("Testing parser...");
        // Basic example of how to use the lexer
        System.out.println("--Basic example--");
        String input = "var x int = 2; var y int = ((3 + 4) * 5));";
        System.out.println("Input: " + input);
        StringReader reader = new StringReader(input); // Create a reader for the input
        Lexer lexer = new Lexer(reader); // Create a lexer for the reader
        Parser parser = new Parser(lexer);
        Program program = parser.getProgram();
        for (Object obj : program.getContent()) {
            System.out.println(obj);
        }
        parseInput(input);

        // More advanced example of how to use the lexer
        System.out.println("--More advanced example--");
        input = "//This is a comment\nvar x int = 2;\nvar y int = 3;\nvar z int = x + y;\nx = 2 + 3;";
        System.out.println("Input: " + input);
        reader = new StringReader(input); // Create a reader for the input
        lexer = new Lexer(reader); // Create a lexer for the reader
        parser = new Parser(lexer);
        program = parser.getProgram();
        for (Object obj : program.getContent()) {
            System.out.println(obj);
        }
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
        reader = new StringReader(input); // Create a reader for the input
        lexer = new Lexer(reader); // Create a lexer for the reader
        parser = new Parser(lexer);
        program = parser.getProgram();
        for (Object obj : program.getContent()) {
            System.out.println(obj);
        }
        parseInput(input);
    }

    public void semanticAnalysis() throws Parser.ParserException {
        System.out.println("Testing semantic Analysis...");
        // Basic example
        System.out.println("--Basic examples--");
        String input = "var x int = 2; var y int = ((3 + 4) * 5);";
        System.out.println("Input: " + input);
        analyseProgram(parseInput(input));
        input = "var x int = 2; x = (3 + 4);";
        System.out.println("Input: " + input);
        analyseProgram(parseInput(input));

        // Basing (wrong) example
        System.out.println("--Basic (wrong) examples--");
        input = "var x int = \"hello\"; var y bool = 10;";
        System.out.println("Input: " + input);
        analyseProgram(parseInput(input));
        input = "var x int = 1; var x int = 2;";
        System.out.println("Input: " + input);
        analyseProgram(parseInput(input));
        input = "var x int = 1; var y int = x + z;";
        System.out.println("Input: " + input);
        analyseProgram(parseInput(input));
        input = "x = 1;";
        System.out.println("Input: " + input);
        analyseProgram(parseInput(input));
        input = "record Point { x int; y int; } var p Point = Point(1, \"a\");"; // Wrong type in record
        System.out.println("Input: " + input);
        analyseProgram(parseInput(input));

        // More advanced example
        System.out.println("--More advanced example--");
        input = "//This is a comment\nvar x int = 2;\nvar y int = 3;\nvar z int = x + y;";
        System.out.println("Input: " + input);
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
    public void writeClassFile(String className, byte[] bytecode) throws CodeGenerationException {
        try {
            FileOutputStream output = new FileOutputStream(new File(className + ".class"));
            output.write(bytecode);
            output.close();
            System.out.println("Generated code saved to "+className+".class");
        } catch (IOException e) {
            e.printStackTrace();
            throw new CodeGenerationException("Failed to write .class file.");
        }
    }


    public void generateCode() throws CodeGenerationException, Parser.ParserException {
        System.out.println("Testing semantic Code Generator...");
        // Basic example
        System.out.println("--Basic examples--");
        String input = "var x int = 2; var y int = ((3 + 4) * 5);";
        System.out.println("Input: " + input);
        Program program = parseInput(input);
        CodeGenerator codeGen = new CodeGenerator();
        String code = codeGen.generateCode(new ProgramCodeGenerator(program)); // This version returns the bytecode as String and not Byte[].
        System.out.println("Generated code:\n" + code);
        byte[] bytecode = codeGen.generateByteCode(new ProgramCodeGenerator(program)); // This version returns the bytecode as bytecode to create the class files
        writeClassFile("Example_1", bytecode);

        // More examples
        input = "var x int = 2; x = (3 + 4);";
        System.out.println("Input: " + input);
        program = parseInput(input);
        code = codeGen.generateCode(new ProgramCodeGenerator(program)); // This version returns the bytecode as String and not Byte[].
        System.out.println("Generated code:\n" + code);
        bytecode = codeGen.generateByteCode(new ProgramCodeGenerator(program)); // This version returns the bytecode as bytecode to create the class files
        writeClassFile("Example_2", bytecode);

        input = "//This is a comment\nvar x int = 2;\nvar y int = 3;\nvar z int = x + y;";
        System.out.println("Input: " + input);
        program = parseInput(input);
        code = codeGen.generateCode(new ProgramCodeGenerator(program)); // This version returns the bytecode as String and not Byte[].
        System.out.println("Generated code:\n" + code);
        bytecode = codeGen.generateByteCode(new ProgramCodeGenerator(program)); // This version returns the bytecode as bytecode to create the class files
        writeClassFile("Example_3", bytecode);

        // Example using "code_example.lang" file
        Path filename = Path.of("code_example.lang");
        System.out.println("--Example using \"code_example.lang\" file--");
        try {
            input = Files.readString(filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        program = parseInput(input);
        code = codeGen.generateCode(new ProgramCodeGenerator(program));
        System.out.println("Generated code:\n" + code);
        bytecode = codeGen.generateByteCode(new ProgramCodeGenerator(program));
        writeClassFile("code_example", bytecode);

    }



    public static void main(String[] args) throws Parser.ParserException, CodeGenerationException {
        Compiler compiler = new Compiler();
        compiler.lexer(args);
        compiler.parser();
        compiler.semanticAnalysis();
        compiler.generateCode();
    }
}