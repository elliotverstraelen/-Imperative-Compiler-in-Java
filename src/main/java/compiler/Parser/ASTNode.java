package compiler.Parser;

import compiler.Exceptions.SemanticException;

public interface ASTNode {
    void accept(ASTVisitor visitor) throws SemanticException;
}

