package compiler.SemanticAnalyser;

import compiler.Parser.*;

import java.util.HashSet;

public class SemanticAnalyzer implements ASTVisitor {

    // To perform semantic analysis, we need to check for duplicate field names. We can do this by keeping track of this with a hashset
    private HashSet<String> recordFields;

    public SemanticAnalyzer(){
        recordFields = new HashSet<>();
    }

    @Override
    public void visit(RecordEntry recordEntry) throws SemanticException {
        // Perform semantic analysis for RecordEntry, duplicate field names
        String fieldName = recordEntry.identifier;
        if (recordFields.contains(fieldName)) {
            throw new SemanticException("Duplicate field name '" + fieldName + "' in record type.");
        } else {
            recordFields.add(fieldName);
        }
    }

    @Override
    public void visit(RecordT recordT) {
        // Perform semantic analysis for RecordT
        // Check for duplicate record type names, for example
    }

    @Override
    public void visit(Program program) {
        //TODO
    }

    @Override
    public void visit(GeneralDecl program) {
        //TODO
    }

    @Override
    public void visit(ProcDecl procDecl) {
        // Implement the semantic analysis for ProcDecl here
    }

    @Override
    public void visit(Param param) {
        //TODO
    }

    @Override
    public void visit(Type type) {
        //TODO
    }

    @Override
    public void visit(Stmt stmt) {
        //TODO
    }

    @Override
    public void visit(Expr expr) {
        //TODO
    }

    @Override
    public void visit(Block block) {
        //TODO
    }

    // Implement other visit methods for different AST nodes as needed
}


