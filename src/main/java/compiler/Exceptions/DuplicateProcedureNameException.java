package compiler.Exceptions;

public class DuplicateProcedureNameException extends SemanticException {
    public DuplicateProcedureNameException(String message) {
        super(message);
    }
}
