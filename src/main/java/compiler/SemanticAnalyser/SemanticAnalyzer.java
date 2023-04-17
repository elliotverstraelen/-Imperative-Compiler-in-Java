package compiler.SemanticAnalyser;

import compiler.Exceptions.*;
import compiler.Parser.*;

import java.util.HashMap;

public class SemanticAnalyzer implements ASTVisitor {

    // To perform semantic analysis, we need to check for duplicate field names. We can do this by keeping track of this with a hashset
    HashMap<String, Type> symbolTable;

    /*
     * Constructor for SemanticAnalyzer
     */
    public SemanticAnalyzer(){
        symbolTable = new HashMap<>();
    }

    /**
     * Visit method for RecordEntry nodes
     * @param recordEntry: RecordEntry node
     * @throws DuplicateFieldException : If duplicate field names are found
     */
    @Override
    public void visit(RecordEntry recordEntry) throws DuplicateFieldException {
        // Perform semantic analysis for RecordEntry, duplicate field names
        String fieldName = recordEntry.getIdentifier();
        if (symbolTable.containsKey(fieldName)){
            throw new DuplicateFieldException("Duplicate field name: " + fieldName);
        }
        symbolTable.put(fieldName, recordEntry.getType());
    }

    /**
     * Perform semantic analysis for RecordT nodes
     * @param recordT: RecordT node to visit
     * @throws DuplicateRecordTypeException: If duplicate record type names are found
     */
    @Override
    public void visit(RecordT recordT) throws DuplicateRecordTypeException {
        String recordTypeName = recordT.getIdentifier();
        if (symbolTable.containsKey(recordTypeName)){
            throw new DuplicateRecordTypeException("Duplicate record type name: " + recordTypeName);
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
        } else if (generalDecl instanceof ConstDecl){
            visit((ConstDecl) generalDecl);
        } else if (generalDecl instanceof RecordT){
            visit((RecordT) generalDecl);
        }
    }

    /**
     * Perform semantic analysis for value/variable/constant declaration nodes
     * @param decl: GeneralDecl node to visit
     * @param declExpr: String representation of the declaration expression
     * @throws SemanticException: If duplicate variable names are found
     */
    public void visit(GeneralDecl decl, String declExpr) throws SemanticException {
        String declName = decl.getIdentifier();
        String declType = decl.getType().toString();
        String valueType = decl.getValue().getType();
        if (symbolTable.containsKey(declName)){
            throw new SemanticException("Duplicate " + declExpr + " name : " + declName);
        }
        // Check if variable type is valid (except for arrays and binary expressions)
        if (!declType.equals("ArrayExpr") && !valueType.equals("BinaryExpr")) {
            if (!declType.equals(valueType)){
                throw new SemanticException("Variable " + declName + " type does not match value type. Found " +
                        valueType + " expected " + declType + ".");
            }
        }
        symbolTable.put(declName, decl.getType());
    }

    /**
     * Perform semantic analysis for constant declaration nodes
     * @param constDecl: ConstDecl node to visit
     * @throws SemanticException: If duplicate variable names are found
     */
    @Override
    public void visit(ConstDecl constDecl) throws SemanticException {
        visit(constDecl, "constant");
    }

    /**
     * Perform semantic analysis for variable declaration nodes
     * Check for duplicate variable names and if the variable type is valid
     * @param varDecl: VarDecl node to visit
     * @throws SemanticException: If duplicate variable names are found
     */
    @Override
    public void visit(VarDecl varDecl) throws SemanticException {
        visit(varDecl, "variable");
    }

    /**
     * Perform semantic analysis for value declaration nodes
     * @param valDecl: ValDecl node to visit
     * @throws SemanticException: If duplicate variable names are found
     */
    @Override
    public void visit(ValDecl valDecl) throws SemanticException {
        visit(valDecl, "value");
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
        // Type nodes do not require semantic analysis, as they are used for type checking.
    }

    /**
     * Perform semantic analysis for statement nodes
     * @param stmt: Stmt node to visit
     */
    @Override
    public void visit(Stmt stmt) {
        // Different types of statements require different semantic analysis.
        if (stmt instanceof For) {
            visit((For) stmt);
        } else if (stmt instanceof CtrlStruct) {
            visit((CtrlStruct) stmt);
        } else if (stmt instanceof ProcCall) {
            visit((ProcCall) stmt);
        } else if (stmt instanceof ReturnStmt) {
            visit((ReturnStmt) stmt);
        } else if (stmt instanceof Assignment) {
            visit((Assignment) stmt);
        }
        // Continue with other statement types as needed
    }

    public void visit(Assignment assignStmt) {
        // Perform semantic analysis for assignment statements here.
        // (e.g., checking for uninitialized variables, type compatibility, etc.)
        //TODO
    }
    public void visit(ArrayAccess arrayAccess) {
        // Perform semantic analysis for array expressions here.
        // (e.g., checking for proper index types, bounds, etc.)

        // Perform semantic analysis for ArrayAccess
        visit(arrayAccess.getIndex());
    }

    public void visit(ReturnStmt returnStmt) {
        // Perform semantic analysis for return statements here.
        // (e.g., checking for correct return type, scope, etc.)
        //TODO
    }

    public void visit(CtrlStruct ifStmt) {
        // Perform semantic analysis for if statements here.
        // (e.g., checking for proper condition types, analyzing then and else blocks, etc.)
        //TODO
    }

    public void visit(For whileStmt) {
        // Perform semantic analysis for while statements here.
        // (e.g., checking for proper condition types, analyzing loop body, etc.)
        //TODO
    }

    public void visit(ProcCall callStmt) {
        // Perform semantic analysis for call statements here.
        // (e.g., checking for proper arguments, procedure existence, etc.)
        //TODO
    }


    /**
     * Perform semantic analysis for expression nodes
     * @param expr: Expr node to visit
     */
    @Override
    public void visit(Expr expr) {
        // Different types of expressions require different semantic analysis.
        if (expr instanceof ArrayExpr) {
            visit((ArrayExpr) expr);
        } else if (expr instanceof BinaryExpr) {
            visit((BinaryExpr) expr);
        } else if (expr instanceof BooleanExpr) {
            visit((BooleanExpr) expr);
        } else if (expr instanceof IdentifierExpr) {
            visit((IdentifierExpr) expr);
        } else if (expr instanceof IntegerExpr) {
            visit((IntegerExpr) expr);
        } else if (expr instanceof RealExpr) {
            visit((RealExpr) expr);
        } else if (expr instanceof RecordExpr) {
            visit((RecordExpr) expr);
        } else if (expr instanceof StringExpr) {
            visit((StringExpr) expr);
        } // Continue with other expression types as needed
    }
    public void visit(ArrayExpr arrayExpr) {
        // Perform semantic analysis for array expressions here.
        // (e.g., checking for proper index types, bounds, etc.)
        //TODO
    }

    public void visit(BinaryExpr booleanExpr) {
        // Perform semantic analysis for boolean expressions here.
        // (e.g., checking for proper boolean values, etc.)
        //TODO
    }
    public void visit(BooleanExpr booleanExpr) {
        // Perform semantic analysis for boolean expressions here.
        // (e.g., checking for proper boolean values, etc.)
        //TODO
    }

    public void visit(IdentifierExpr identifierExpr) {
        // Perform semantic analysis for identifier expressions here.
        // (e.g., checking for undeclared identifiers, proper use, etc.)
        //TODO
    }

    public void visit(IntegerExpr integerExpr) {
        // Perform semantic analysis for integer expressions here.
        // (e.g., checking for proper integer values, etc.)
        //TODO
    }

    public void visit(RealExpr realExpr) {
        // Perform semantic analysis for real expressions here.
        // (e.g., checking for proper real values, etc.)
        //TODO
    }

    public void visit(RecordExpr recordExpr) {
        // Perform semantic analysis for record expressions here.
        // (e.g., checking for proper record field access, etc.)
        //TODO
    }

    public void visit(StringExpr stringExpr) {
        // Perform semantic analysis for string expressions here.
        // (e.g., checking for proper string values, etc.)
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


