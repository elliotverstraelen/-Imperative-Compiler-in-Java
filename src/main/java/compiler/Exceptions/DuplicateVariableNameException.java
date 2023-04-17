package compiler.Exceptions;

public class DuplicateVariableNameException extends SemanticException {
    public DuplicateVariableNameException(String message) {
        super(message);
    }
}
