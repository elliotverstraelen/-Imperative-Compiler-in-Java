package compiler.Exceptions;

public class UndefinedIdentifierException extends SemanticException {
    public UndefinedIdentifierException(String message) {
        super(message);
    }
}
