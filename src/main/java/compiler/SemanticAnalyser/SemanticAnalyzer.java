package compiler.SemanticAnalyser;

import compiler.Exceptions.*;
import compiler.Parser.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
     * Visit method for RecordDecl nodes (records and their fields)
     * @param recordDecl: RecordDecl node
     * @throws DuplicateRecordTypeException: If duplicate record types are found
     * @throws DuplicateFieldException: If duplicate field names are found
     */
    @Override
    public void visit(RecordDecl recordDecl) throws DuplicateRecordTypeException, DuplicateFieldException {
        // Perform semantic analysis for RecordDecl
        String recordTypeName = recordDecl.getName();
        if (symbolTable.containsKey(recordTypeName)){
            throw new DuplicateRecordTypeException("Duplicate record type name: " + recordTypeName);
        }
        symbolTable.put(recordTypeName, new Type("Record"));
        for (int i = 0; i < recordDecl.getFields().size(); i++) {
            RecordEntry e = recordDecl.getFields().get(i);
            // Check for duplicate field names
            if (recordDecl.getFields().stream().filter(x -> x.getIdentifier().equals(e.getIdentifier())).count() > 1) {
                throw new DuplicateFieldException("Duplicate field name in record " + recordTypeName + ": "
                        + e.getIdentifier());
            }
            String fieldName = recordTypeName + "." + e.getIdentifier() + "." + i; // Stored as recordType.fieldName.index
            symbolTable.put(fieldName, e.getType());
        }
    }

    /**
     * Perform semantic analysis for Program nodes
     * @param program: Program node to visit
     * @throws SemanticException: If duplicates are found
     */
    @Override
    public void visit(Program program) throws SemanticException {
        for (Object obj : program.getContent()) {
            if (obj instanceof GeneralDecl) {
                ((GeneralDecl) obj).accept(this);
            } else if (obj instanceof RecordDecl) {
                ((RecordDecl) obj).accept(this);
            } else if (obj instanceof Stmt) {
                ((Stmt) obj).accept(this);
            } else {
                throw new RuntimeException("Unexpected object in program content : " + obj);
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
        } else if (generalDecl instanceof ArrayAssignment){
            visit((ArrayAssignment) generalDecl);
        } else if (generalDecl instanceof Assignment){
            visit((Assignment) generalDecl);
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
        String declType = decl.getType().getName();
        if (declType.equals("BinaryExpr")){
            declType = visit((BinaryExpr) decl.getValue());
        }
        if (decl.getValue() == null){
            throw new UninitializedVariableException("Variable " + declName + " not initialized.");
        }
        String valueType = decl.getValue().getType().getName();
        if (valueType.equals("BinaryExpr")){
            valueType = visit((BinaryExpr) decl.getValue());
        }
        // Check if variable name is already in symbol table
        if (symbolTable.containsKey(declName)){
            throw new DuplicateVariableNameException("Duplicate " + declExpr + " name : " + declName);
        }
        // Check if variable type is valid
        if (declType.equals("BinaryExpr")) {
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
        // Check if the variable is a record, if so, check the records fields
        if (symbolTable.containsKey(declType)){
            String recordType = declType;
            RecordExpr recordExpr = (RecordExpr) decl.getValue();
            // Sort the fields of the record by their identifier
            ArrayList<RecordEntry> sortedRecord = recordExpr.getContent();
            sortedRecord.sort(Comparator.comparing(RecordEntry::getIdentifier));

            List<String> list = new ArrayList<>(symbolTable.keySet().stream().filter(x -> x.contains(recordType)).toList());
            list.remove(recordType); // Remove the record type itself
            for (int i = 0; i < list.size(); i++){
                String field = list.get(i);
                // Correct field type
                declType = symbolTable.get(field).getName();
                // Field index (recordType.fieldName.index)
                int index = Integer.parseInt(field.substring(field.lastIndexOf(".") + 1));
                // Value type
                valueType = sortedRecord.get(index).getType().getName();
                if (!declType.equals(valueType)) {
                    throw new SemanticException("Field " + field.substring(0, field.lastIndexOf(".")) +
                            " type does not match value type for '" + declName + "'. Found " + valueType + " expected "
                            + declType + ".");
                }
            }
        }
        // If the variable is an array declaration, add the size to the type string
        if (decl.getValue() instanceof ArrayExpr declArray){
            symbolTable.put(declName,  new Type(declType + declArray.getSize()));
        } else {
            symbolTable.put(declName, decl.getType());
        }
    }

    /**
     * Perform semantic analysis for constant declaration nodes
     * @param constDecl: ConstDecl node to visit
     * @throws SemanticException: If duplicate variable names are found
     */
    @Override
    public void visit(ConstDecl constDecl) throws SemanticException {
        visit(constDecl, "const");
    }

    /**
     * Perform semantic analysis for variable declaration nodes
     * Check for duplicate variable names and if the variable type is valid
     * @param varDecl: VarDecl node to visit
     * @throws SemanticException: If duplicate variable names are found
     */
    @Override
    public void visit(VarDecl varDecl) throws SemanticException {
        visit(varDecl, "var");
    }

    /**
     * Perform semantic analysis for value declaration nodes
     * @param valDecl: ValDecl node to visit
     * @throws SemanticException: If duplicate variable names are found
     */
    @Override
    public void visit(ValDecl valDecl) throws SemanticException {
        visit(valDecl, "val");
    }

    /**
     * Perform semantic analysis for procedure declaration nodes
     * @param procDecl: ProcDecl node to visit
     * @throws SemanticException: If duplicate procedure names are found
     */
    @Override
    public void visit(ProcDecl procDecl) throws SemanticException {
        // Check if procedure name is already in symbol table
        String procName = procDecl.getIdentifier();
        if (symbolTable.containsKey(procName)){
            throw new DuplicateProcedureNameException("Duplicate procedure name: " + procName);
        }

        // Check if procedure has a return type, if so, check if there is a return statement in the body
        boolean containsReturn = false;
        if (procDecl.getType() != null || procDecl.getType().getName().equals("void")) {
            ReturnStmt returnStmt = new ReturnStmt(null);
            for (Object stmt : procDecl.getBody().getStatements()) {
                if (stmt instanceof ReturnStmt) {
                    containsReturn = true;
                    returnStmt = (ReturnStmt) stmt;
                    break;
                }
            }
            if (!containsReturn){
                throw new ReturnTypeMismatchException("Procedure " + procName + " has a return type, but does not contain a return statement.");
            } else {
                // Check if return type matches the type of the return statement
                String procType = procDecl.getType().getName();
                String returnType = returnStmt.getValue().getType().getName();
                if (!procType.equals(returnType)) {
                    throw new ReturnTypeMismatchException("Procedure " + procName + " return type does not match return statement type. Found " +
                            returnType + " expected " + procType + ".");
                }
            }
        } else {
            // If procedure has no return type, check if there is a return statement in the body
            for (Object stmt : procDecl.getBody().getStatements()) {
                if (stmt instanceof ReturnStmt) {
                    containsReturn = true;
                    break;
                }
            }
            if (containsReturn){
                throw new ReturnTypeMismatchException("Procedure " + procName + " has no return type, but contains a return statement.");
            }
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
        }
    }

    public void visit(Assignment assignStmt) throws SemanticException {
        // Perform semantic analysis for assignment statements here.
        // (e.g., checking for uninitialized variables, type compatibility, etc.)
        //String varName = assignStmt.getLeft();
        String identifier = assignStmt.getIdentifier();
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

    public void visit(ArrayAssignment assignStmt) throws SemanticException {
        // Perform semantic analysis for array assignment here.
        // (e.g., checking for proper index types, bounds, etc.)
        String identifier = assignStmt.getIdentifier();
        if (!symbolTable.containsKey(identifier)){
            throw new SemanticException("Variable " + identifier + " is not initialized.");
        }
        String varType = symbolTable.get(identifier).toString();
        String size = varType.substring(varType.indexOf(":") + 1, varType.indexOf("}"));
        Expr index = assignStmt.getIndex();
        Expr assignValue = assignStmt.getValue();

        // Ensure the index expression is an integer type
        Type indexExprType = index.getType();
        if (!indexExprType.getName().equals("int")) {
            throw new SemanticException("Invalid array access: Index expression type should be int, found " + indexExprType);
        }

        String indexType = index.toString();
        String indexValue = indexType.substring(indexType.indexOf(":") + 1, indexType.indexOf("}"));
        // Ensure that the index is within the bounds of the array
        if (Integer.parseInt(indexValue) > Integer.parseInt(size)) {
            throw new SemanticException("Invalid array access: Index " + size + " out of bounds.");
        }
        
        // Ensure that the type of the value being assigned matches the type of the array
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
        }
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

        // Identify the type of the left hand side of the expression
        String leftType = binaryExpr.getLeft().getType().getName();
        if (leftType.equals("BinaryExpr")) {
            leftType = visit((BinaryExpr) binaryExpr.getLeft());
        }

        if (leftType.equals("identifier")) {
            try {
                leftType = symbolTable.get(((IdentifierExpr) binaryExpr.getLeft()).getIdentifier()).getName();
            } catch (NullPointerException e) {
                throw new UndefinedIdentifierException("Undeclared identifier: " + ((IdentifierExpr) binaryExpr.getLeft()).getIdentifier());
            }
        }

        // Identify the type of the right hand side of the expression
        String rightType = binaryExpr.getRight().getType().getName();
        if (rightType.equals("BinaryExpr")) {
            rightType = visit((BinaryExpr) binaryExpr.getRight());
        }

        if (rightType.equals("identifier")) {
            try {
                rightType = symbolTable.get(((IdentifierExpr) binaryExpr.getRight()).getIdentifier()).getName();
            } catch (NullPointerException e) {
                throw new UndefinedIdentifierException("Undeclared identifier: " + ((IdentifierExpr) binaryExpr.getRight()).getIdentifier());
            }
        }

        // Check if the two types are the same
        if (!leftType.equals(rightType)) {
            // If the types are not the same, check if they are both integers or reals
            if (leftType.equals("int") && rightType.equals("real")) {
                // If the left type is integer and the right type is real, the expression is valid
                return "real";
            } else if (leftType.equals("real") && rightType.equals("int")) {
                // If the left type is real and the right type is integer, the expression is valid
                return "real";
            }
            throw new UndefinedIdentifierException("Type mismatch. Found " + leftType + " and " + rightType + ".");
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

    public void visit(RecordExpr recordExpr) throws  SemanticException {
        // Placeholder
    }

    /**
     * Perform semantic analysis for boolean expressions nodes
     * @param booleanExpr: BooleanExpr node to visit
     * @throws SemanticException
     */
    public void visit(BooleanExpr booleanExpr) throws  SemanticException {
        // Check that the boolean expression is valid
        if (!booleanExpr.getType().getName().equals("bool")) {
            throw new SemanticException("Invalid boolean expression: Expected bool, found " + booleanExpr.getType().getName());
        }
    }
    /**
     * Perform semantic analysis for block nodes
     * @param block: Block node to visit
     */
    @Override
    public void visit(Block block) throws SemanticException {
        // Analyze each statement in the block
        for (Object stmt : block.getStatements()) {
            ((ASTNode) stmt).accept(this);
        }
    }
}


