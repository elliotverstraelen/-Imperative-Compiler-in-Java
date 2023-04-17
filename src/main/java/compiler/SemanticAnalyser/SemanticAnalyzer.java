package compiler.SemanticAnalyser;

import compiler.Parser.*;

import java.util.HashMap;

public class SemanticAnalyzer implements ASTVisitor {

    // To perform semantic analysis, we need to check for duplicate field names. We can do this by keeping track of this with a hashset
    HashMap<String, Type> symbolTable;

    /**
     * Constructor for SemanticAnalyzer
     */
    public SemanticAnalyzer(){
        symbolTable = new HashMap<>();
    }

    /**
     * Visit method for RecordEntry
     * @param recordEntry: RecordEntry node
     * @throws SemanticException: If duplicate field names are found
     */
    @Override
    public void visit(RecordEntry recordEntry) throws SemanticException {
        // Perform semantic analysis for RecordEntry, duplicate field names
        String fieldName = recordEntry.identifier;
        if (symbolTable.containsKey(fieldName)){
            throw new SemanticException("Duplicate field name: " + fieldName);
        }
        symbolTable.put(fieldName, recordEntry.type);
    }

    /**
     * Perform semantic analysis for RecordT
     * @param recordT: RecordT node to visit
     * @throws SemanticException: If duplicate record type names are found
     */
    @Override
    public void visit(RecordT recordT) throws SemanticException {
        String recordTypeName = recordT.identifier;
        if (symbolTable.containsKey(recordTypeName)){
            throw new SemanticException("Duplicate record type name: " + recordTypeName);
        }
        symbolTable.put(recordTypeName, recordT.getType());
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


