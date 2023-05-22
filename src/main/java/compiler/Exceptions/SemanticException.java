package compiler.Exceptions;

public class SemanticException extends CompilerException {
    public SemanticException(String message) {
        super(message);
    }

    public SemanticException(String message, Throwable cause) {
        super(message, cause);
    }
}

