package compiler.Exceptions;

public class UninitializedVariableException extends SemanticException {
    public UninitializedVariableException(String message) {
        super(message);
    }
}
