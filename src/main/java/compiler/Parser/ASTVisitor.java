package compiler.Parser;

import compiler.Exceptions.SemanticException;

public interface ASTVisitor {
    void visit(Program program) throws SemanticException;
    void visit(GeneralDecl generalDecl) throws SemanticException;

    void visit(ValDecl valDecl) throws SemanticException;

    void visit(ConstDecl constDecl) throws SemanticException;

    void visit(VarDecl varDecl) throws SemanticException;

    void visit(ProcDecl procDecl) throws SemanticException;

    void visit(Param param) throws SemanticException;

    void visit(Stmt stmt) throws SemanticException;

    void visit(Expr expr) throws SemanticException;

    void visit(Block block) throws SemanticException;

    void visit(RecordDecl recordDecl) throws SemanticException;
}
