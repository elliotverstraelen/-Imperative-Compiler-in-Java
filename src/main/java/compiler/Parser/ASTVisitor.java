package compiler.Parser;

import compiler.SemanticAnalyser.SemanticException;

public interface ASTVisitor {
    void visit(Program program);
    void visit(GeneralDecl generalDecl);
    void visit(RecordT recordT);
    void visit(RecordEntry recordEntry) throws SemanticException;
    void visit(ProcDecl procDecl);

    void visit(Param param);

    void visit(Type type);

    void visit(Stmt stmt);

    void visit(Expr expr);

    void visit(Block block);
    // Add other visit methods for other ASTNode types
}
