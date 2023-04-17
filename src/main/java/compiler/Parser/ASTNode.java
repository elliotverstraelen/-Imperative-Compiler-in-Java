package compiler.Parser;

import compiler.SemanticAnalyser.SemanticException;

public interface ASTNode {
    void accept(ASTVisitor visitor) throws SemanticException;
}

