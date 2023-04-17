package compiler.SemanticAnalyser;

import compiler.Exceptions.*;
import compiler.Parser.*;
import java.util.ArrayList;

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
        for (Object obj : program.getContent()){
            if (obj instanceof GeneralDecl) {
                ((GeneralDecl) obj).accept(this);
            } else if (obj instanceof Stmt){
                ((Stmt) obj).accept(this);
            } else {
                throw new RuntimeException("Unexpected object in program content");
            }
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
        String declType = decl.getValue().toString();
        if (declType.equals("BinaryExpr")){
            declType = visit((BinaryExpr) decl.getValue());
        }
        String valueType = decl.getValue().getType().toString();
        if (valueType.equals("BinaryExpr")){
            valueType = visit((BinaryExpr) decl.getValue());
        }
        // Check if variable name is already in symbol table
        if (symbolTable.containsKey(declName)){
            throw new DuplicateVariableNameException("Duplicate " + declExpr + " name : " + declName);
        }
        // Check if variable type is valid (except for arrays and binary expressions)
        if (declType.equals("ArrayExpr") || declType.equals("BinaryExpr")) {
            // Check if variable type matches value type, if it is an identifier, check if the identifier type matches the variable type
            if (valueType.equals("IdentifierExpr")) {
                String identifierType = symbolTable.get(decl.getValue().toString()).toString();
                if (!declType.equals(identifierType)) {
                    throw new SemanticException("Variable " + declName + " type does not match value type. Found " +
                            identifierType + " expected " + declType + ".");
                }
            } else if (valueType.equals("BinaryExpr")){
                // Check that every member of the binary expression is of the same type
                if (!declType.equals(visit((BinaryExpr) decl.getValue()))){
                    throw new SemanticException("Variable " + declName + " type does not match value type. Found " +
                            valueType + " expected " + declType + ".");
                }
            } else if (!decl.getType().getName().contains(valueType)){
                throw new SemanticException("Variable " + declName + " type does not match value type. Found " +
                        valueType + " expected " + declType + ".");
            }
        } else if (!declType.equals(valueType)){
            throw new SemanticException("Variable " + declName + " type does not match value type. Found " +
                    valueType + " expected " + declType + ".");
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
            throw new DuplicateProcedureNameException("Duplicate procedure name: " + procName);
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
            throw new DuplicateParameterNameException("Duplicate parameter name: " + paramName);
        }
        symbolTable.put(paramName, param.getType());
    }

    /**
     * Perform semantic analysis for statement nodes
     * @param stmt: Stmt node to visit
     */
    @Override
    public void visit(Stmt stmt) throws SemanticException {
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
    }

    public void visit(Assignment assignStmt) throws SemanticException {
        // Perform semantic analysis for assignment statements here.
        // (e.g., checking for uninitialized variables, type compatibility, etc.)
        //String varName = assignStmt.getLeft();
        String identifier = ((Left) assignStmt.getLeft()).getIdentifier();
        if (!symbolTable.containsKey(identifier)){
            throw new SemanticException("Variable " + identifier + " is not initialized.");
        }
        String varType = symbolTable.get(identifier).toString();
        Expr assignValue = assignStmt.getValue();
        if (assignValue instanceof BinaryExpr binary) {
            String rightType = binary.getRight().getType().toString();
            String leftType = binary.getLeft().getType().toString();
            if (!varType.equals(rightType)) {
                throw new SemanticException("Variable " + identifier + " type does not match value type. Found " +
                        rightType + " expected " + varType + ".");
            } else if (!varType.equals(leftType)) {
                throw new SemanticException("Variable " + identifier + " type does not match value type. Found " +
                        leftType + " expected " + varType + ".");
            }
        } else if (assignValue instanceof IdentifierExpr) {
            String assignType = assignValue.getType().toString();
            if (!varType.equals(assignType)) {
                throw new SemanticException("Variable " + identifier + " type does not match value type. Found " +
                        assignType + " expected " + varType + ".");
            }
        } else {
            String assignType = assignValue.getType().toString();
            if (!varType.equals(assignType)) {
                throw new SemanticException("Variable " + identifier + " type does not match value type. Found " +
                        assignType + " expected " + varType + ".");
            }
        }
    }
    public void visit(ArrayAccess arrayAccess, Type arrayExprType) throws SemanticException {
        // Perform semantic analysis for array expressions here.
        // (e.g., checking for proper index types, bounds, etc.)

        // Perform semantic analysis for ArrayAccess
        // Analyze the array expression and the index expression
        arrayAccess.getIndex().accept(this);

        // Ensure the array expression is an array type
        if (!arrayExprType.getName().endsWith("[]")) {
            throw new SemanticException("Invalid array access: " + arrayExprType + " is not an array type.");
        }

        // Ensure the index expression is an integer type
        Type indexExprType = arrayAccess.getIndex().getType();
        if (!indexExprType.getName().equals("int")) {
            throw new SemanticException("Invalid array access: Index expression type should be int, found " + indexExprType);
        }

    }

    public void visit(ReturnStmt returnStmt) throws SemanticException {
        // Perform semantic analysis for return statements here.
        // (e.g., checking for correct return type, scope, etc.)
        Expr returnValue = returnStmt.getValue();
        if (returnValue != null) {
            returnValue.accept(this);
        }
    }

    public void visit(CtrlStruct ifStmt) throws SemanticException {
        // Perform semantic analysis for if statements here.
        // (e.g., checking for proper condition types, analyzing then and else blocks, etc.)
        // Analyze the condition expression

        Expr condition = ifStmt.getCondition();
        condition.accept(this);

        // Ensure the condition expression is of type boolean
        Type conditionType = condition.getType();
        if(!conditionType.getName().equals("boolean")) {
            throw new SemanticException("Invalid if condition: expected boolean, found " + conditionType);
        }

        // Analyze th then block
        Block thenBlock = ifStmt.getBody();
        thenBlock.accept(this);

        // Analyze the else block (if present)
        Block elseBlock = ifStmt.getElseBody();
        if (elseBlock != null){
            elseBlock.accept(this);
        }
    }

    public void visit(For forStmt) throws SemanticException {
        // Perform semantic analysis for while statements here.
        // (e.g., checking for proper condition types, analyzing loop body, etc.)

        // Analyze the loop condition expression
        Expr loopCondition = forStmt.getCondition();
        loopCondition.accept(this);

        // Analyze th loop step expression
        Expr loopStep = forStmt.getStep();
        loopStep.accept(this);

        // Analyze the loop body block
        Block loopBody = forStmt.getBody();
        loopBody.accept(this);
    }

    public void visit(ProcCall callStmt) throws SemanticException {
        // Perform semantic analysis for call statements here.
        // (e.g., checking for proper arguments, procedure existence, etc.)
        // Why does IntelliJ think that procDeclObj ca't be a ProcDecl

        // Check if the procedure exists
        String identifier = callStmt.getIdentifier();
        if(!symbolTable.containsKey(identifier)){
            throw new SemanticException("Undefined procedure: " + identifier);
        }
        Object procDeclObj = symbolTable.get(identifier); // changed to object type

        // Ensure the object has a "ProcDecl" type
        if(!(procDeclObj instanceof ProcDecl)) {
            throw new SemanticException("Invalid procedure call: " + identifier + " is not a procedure.");
        }

        ProcDecl procDecl = (ProcDecl) procDeclObj;

        // Analyze the procedure arguments
        ArrayList<Expr> arguments = callStmt.getArgs();
        for (Expr arg : arguments) {
            arg.accept(this);
        }

        // Check the types and number of the arguments against the procedure declaration
        ArrayList<Param> expectedParams = procDecl.getParams();

        // Check the number of arguments
        if (expectedParams.size() != arguments.size()) {
            throw new SemanticException("Incorrect number of arguments for procedure " + identifier + ": Expected " +
                    expectedParams.size() + ", found " + arguments.size());
        }

        // Check the types of arguments
        for (int i = 0; i < arguments.size(); i++) {
            Type expectedType = expectedParams.get(i).getType();
            Type actualType = arguments.get(i).getType();
            if (!expectedType.equals(actualType)) {
                throw new SemanticException("Type mismatch for argument " + (i + 1) + " of procedure " + identifier +
                        ": Expected " + expectedType + ", found " + actualType);
            }
        }
    }


    /**
     * Perform semantic analysis for expression nodes
     * @param expr: Expr node to visit
     */
    @Override
    public void visit(Expr expr) throws SemanticException {
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
    public void visit(ArrayExpr arrayExpr) throws SemanticException {
        // Perform semantic analysis for array expressions here.
        // (e.g., checking for proper index types, bounds, etc.)
        // Analyze the elements of the Array
        for (Expr e : arrayExpr.getContent()){
            e.accept(this);
        }
        // Ensure that all elements of the array are of the expected type
        Type expectedType = arrayExpr.getType();
        for (Expr e : arrayExpr.getContent()) {
            if (!e.getType().equals(expectedType)) {
                throw new SemanticException("Array element type mismatch: Expected " + expectedType + ", found " + e.getType());
            }
        }
    }

    /**
     * Return the expression type of binary expression, or throw an exception if the expression is invalid
     * @param binaryExpr: BinaryExpr node to visit
     * @return String: Type of the expression
     */
    public String visit(BinaryExpr binaryExpr) throws SemanticException {
        // Perform semantic analysis for binary expressions
        String leftType = binaryExpr.getLeft().getType().toString();
        if (leftType.equals("BinaryExpr")) {
            leftType = visit((BinaryExpr) binaryExpr.getLeft());
        }
        String rightType = binaryExpr.getRight().getType().toString();
        if (rightType.equals("BinaryExpr")) {
            rightType = visit((BinaryExpr) binaryExpr.getRight());
        }

        // Identify the type of the left hand of the expression
        if (leftType.equals("IdentifierExpr")) {
            String identifier = ((IdentifierExpr) binaryExpr.getLeft()).getIdentifier();
            if (!symbolTable.containsKey(identifier)) {
                throw new SemanticException("Variable " + identifier + " is not initialized.");
            }
            leftType = symbolTable.get(identifier).toString();
        } else {
            leftType = binaryExpr.getLeft().getType().toString();
            if (leftType.equals("BinaryExpr")) {
                leftType = visit((BinaryExpr) binaryExpr.getLeft());
            }
        }

        // Identify the type of the right hand of the expression
        if (rightType.equals("IdentifierExpr")) {
            String identifier = ((IdentifierExpr) binaryExpr.getRight()).getIdentifier();
            if (!symbolTable.containsKey(identifier)) {
                throw new SemanticException("Variable " + identifier + " is not initialized.");
            }
            rightType = symbolTable.get(identifier).toString();
        } else {
            rightType = binaryExpr.getRight().getType().toString();
            if (rightType.equals("BinaryExpr")) {
                rightType = visit((BinaryExpr) binaryExpr.getLeft());
            }
        }

        // Check if the two types are the same
        if (!leftType.equals(rightType)) {
            throw new SemanticException("Type mismatch. Found " + leftType + " and " + rightType + ".");
        }
        return leftType;
    }

    public void visit(IdentifierExpr identifierExpr) throws SemanticException {
        // Check if the identifier is declared
        String identifier = identifierExpr.getIdentifier();
        if(!symbolTable.containsKey(identifier)) {
            throw new SemanticException("Undeclared identifier: " + identifier);
        }
        // Get the type of the identifier from the symbol table
        identifierExpr.setType(symbolTable.get(identifier));
    }

    /**
     * Perform semantic analysis for block nodes
     * @param block: Block node to visit
     */
    @Override
    public void visit(Block block) throws SemanticException {
        // Analyze each statement in the block
        for (Stmt stmt : block.getStatements()){
            stmt.accept(this);
        }
    }
}


