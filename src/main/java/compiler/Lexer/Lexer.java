package compiler.Lexer;
import java.io.Reader;

public class Lexer {

    private Reader sourceFile;
    private Symbol currentSymbol
    
    public Lexer(Reader input) {
        this.sourceFile = sourceFile;
        this.currentSymbol = NULL // Is this correct instanciation ?
    }
    private int readNextChar() throws IOException {
        return this.currentChar = this.sourceFile.read();
    }
    
    public Symbol getNextSymbol() throws IOException {
        //Implement the logic
    }

}
