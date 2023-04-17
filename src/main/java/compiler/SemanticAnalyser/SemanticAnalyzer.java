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
     * Visit method for RecordEntry nodes
     * @param recordEntry: RecordEntry node
     * @throws SemanticException: If duplicate field names are found
     */
    @Override
    public void visit(RecordEntry recordEntry) throws SemanticException {
        // Perform semantic analysis for RecordEntry, duplicate field names
        String fieldName = recordEntry.getIdentifier();
        if (symbolTable.containsKey(fieldName)){
            throw new SemanticException("Duplicate field name: " + fieldName);
        }
        symbolTable.put(fieldName, recordEntry.getType());
    }

    /**
     * Perform semantic analysis for RecordT nodes
     * @param recordT: RecordT node to visit
     * @throws SemanticException: If duplicate record type names are found
     */
    @Override
    public void visit(RecordT recordT) throws SemanticException {
        String recordTypeName = recordT.getIdentifier();
        if (symbolTable.containsKey(recordTypeName)){
            throw new SemanticException("Duplicate record type name: " + recordTypeName);
        }
        symbolTable.put(recordTypeName, recordT.getType());
    }

    /**
     * Perform semantic analysis for Program nodes
     * @param program: Program node to visit
     * @throws SemanticException: If duplicates are found
     */
    @Override
    public void visit(Program program) throws SemanticException {
        for (GeneralDecl declaration : program.getDeclarations()){
            declaration.accept(this);
        }
    }

    /**
     * Perform semantic analysis for general declaration nodes
     * @param generalDecl: GeneralDecl node to visit
     */
    @Override
    public void visit(GeneralDecl generalDecl) throws SemanticException {
        // Redirect to corresponding visit method
        if (generalDecl instanceof ValDecl){
            visit((ValDecl) generalDecl);
        } else if (generalDecl instanceof VarDecl){
            visit((VarDecl) generalDecl);
        } else if (generalDecl instanceof ProcDecl){
            visit((ProcDecl) generalDecl);
        }
    }

    /**
     * Perform semantic analysis for value declaration nodes
     * @param valDecl: ValDecl node to visit
     * @throws SemanticException: If duplicate variable names are found
     */
    @Override
    public void visit(ValDecl valDecl) throws SemanticException {
        String varName = valDecl.getIdentifier();
        if (symbolTable.containsKey(varName)){
            throw new SemanticException("Duplicate variable name: " + varName);
        }
        symbolTable.put(varName, valDecl.getType());
    }

    /**
     * Perform semantic analysis for variable declaration nodes
     * @param varDecl: VarDecl node to visit
     * @throws SemanticException: If duplicate variable names are found
     */
    @Override
    public void visit(VarDecl varDecl) throws SemanticException {
        String varName = varDecl.getIdentifier();
        if (symbolTable.containsKey(varName)){
            throw new SemanticException("Duplicate variable name: " + varName);
        }
        symbolTable.put(varName, varDecl.getType());
    }

    /**
     * Perform semantic analysis for procedure declaration nodes
     * @param procDecl: ProcDecl node to visit
     * @throws SemanticException: If duplicate procedure names are found
     */
    @Override
    public void visit(ProcDecl procDecl) throws SemanticException {
        String procName = procDecl.getIdentifier();
        if (symbolTable.containsKey(procName)){
            throw new SemanticException("Duplicate procedure name: " + procName);
        }
        symbolTable.put(procName, procDecl.getType());
    }

    /**
     * Perform semantic analysis for parameters nodes
     * @param param: Param node to visit
     * @throws SemanticException: If duplicate parameter names are found
     */
    @Override
    public void visit(Param param) throws SemanticException {
        String paramName = param.getName();
        if (symbolTable.containsKey(paramName)){
            throw new SemanticException("Duplicate parameter name: " + paramName);
        }
        symbolTable.put(paramName, param.getType());
    }

    /**
     * Perform semantic analysis for type nodes
     * @param type: Type node to visit
     */
    @Override
    public void visit(Type type) {
        //TODO
    }

    /**
     * Perform semantic analysis for statement nodes
     * @param stmt: Stmt node to visit
     */
    @Override
    public void visit(Stmt stmt) {
        //TODO
    }

    /**
     * Perform semantic analysis for expression nodes
     * @param expr: Expr node to visit
     */
    @Override
    public void visit(Expr expr) {
        //TODO
    }

    /**
     * Perform semantic analysis for block nodes
     * @param block: Block node to visit
     */
    @Override
    public void visit(Block block) {
        //TODO
    }

    // Implement other visit methods for different AST nodes as needed
    // ctrlStruct, expressions, etc.
}


