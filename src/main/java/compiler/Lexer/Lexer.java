package compiler.Lexer;
import java.io.Reader;

public class Lexer {

    private Reader sourceFile;
    private int currentChar
    
    public Lexer(Reader input) {
        this.sourceFile = sourceFile;
        this.currentSymbol = NULL // Is this correct instanciation ?
    }
    private int readNextChar() throws IOException {
        return this.currentChar = this.sourceFile.read();
    }
    
    public Symbol getNextSymbol() throws IOException {
        StringBuilder lexemeBuilder = new StringBuilder();
        int state = 0;

        while(true) {
            switch (state) {
                case 0:
                    // TODO
                    // Initial State
                    break;
                case 1:
                    // TODO
                    // Identifiers
                    break;
                case 2:
                    // TODO
                    // Integers
                    break;
            }
        }
    }

}
