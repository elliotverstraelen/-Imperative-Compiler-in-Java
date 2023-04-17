package compiler.Exceptions;

public class DuplicateParameterNameException extends SemanticException {
    public DuplicateParameterNameException(String message) {
        super(message);
    }
}
