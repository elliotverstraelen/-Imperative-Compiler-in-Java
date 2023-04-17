package compiler.Exceptions;

public class DuplicateFieldException extends SemanticException {
    public DuplicateFieldException(String message) {
        super(message);
    }
}
