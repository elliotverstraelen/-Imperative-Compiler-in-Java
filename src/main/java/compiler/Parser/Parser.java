package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static compiler.Lexer.Lexer.Token.*;

public class Parser {
    private final Lexer lexer; // Lexer for the parser
    private Symbol lookahead; // Lookahead symbol

    private final Program program; // Parsed program, as a list of record declarations, a list of variable declarations, and a list of procedures declarations

    public static class ParserException extends Exception {
        public ParserException(String message) {
            super(message);
        }
    }

    /**
     * Constructor for Parser
     * @param lexer - Lexer for the parser
     */
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.lookahead = lexer.currentSymbol;
        this.program = new Program(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        try {
            parseProgram();
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Matches a token with the lookahead symbol
     * @param token - Token to match
     * @throws ParserException - If the token doesn't match
     */
    private Symbol match(Lexer.Token token) throws ParserException {
        if (lookahead.getToken() != token) {
            throw new ParserException("Expected " + token + " but got " + lookahead.getToken());
        } else {
            Symbol matchingSymbol = lookahead;
            this.lookahead = lexer.getNextSymbol();
            return matchingSymbol;
        }
    }

    /**
     * Parses the program
     * A program consists of a list of record declarations, a list of variable declarations, and a list of procedures declarations
     */
    public void parseProgram() throws ParserException {
        while (lookahead.getToken() != EOF) {
            switch (lookahead.getToken()) {
                case KEYWORD_RECORD -> this.program.add(parseRecordDecl());
                case KEYWORD_CONST, KEYWORD_VAR, KEYWORD_VAL -> this.program.add(parseGeneralDecl());
                case IDENTIFIER -> this.program.add(parseAssignment());
                case KEYWORD_PROC -> this.program.add(parseProcDecl());
                case SYMBOL_SEMICOLON -> match(SYMBOL_SEMICOLON);
                default -> throw new ParserException("Expected a declaration but got " + lookahead.getToken());
            }
        }
    }

    /**
     * Parses a record declaration
     * Grammar: RecordDecl -> record Identifier { RecordFields } ;
     * @return RecordDecl - Record declaration
     */
    private RecordDecl parseRecordDecl() throws ParserException {
        match(KEYWORD_RECORD);
        Symbol identifier = match(IDENTIFIER);
        match(SYMBOL_LEFT_BRACE);
        ArrayList<RecordEntry> recordFields = parseRecordFields();
        match(SYMBOL_RIGHT_BRACE);
        return new RecordDecl(identifier.getLexeme(), recordFields);
    }

    /**
     * Parses record fields
     * Grammar: RecordFields -> RecordField ; RecordFields | RecordField
     * @return ArrayList<Type> - List of record fields
     */
    private ArrayList<RecordEntry> parseRecordFields() throws ParserException {
        ArrayList<RecordEntry> recordFields = new ArrayList<>();
        String identifier = match(IDENTIFIER).getLexeme();
        Type type = parseType();
        recordFields.add(new RecordEntry(identifier, type.getName(), null));
        match(SYMBOL_SEMICOLON);
        while (lookahead.getToken() != SYMBOL_RIGHT_BRACE) {
            identifier = match(IDENTIFIER).getLexeme();
            type = parseType();
            recordFields.add(new RecordEntry(identifier, type.getName(), null));
            match(SYMBOL_SEMICOLON);
        }
        return recordFields;
    }

    /**
     * Parses a type
     * Grammar : Type -> "int" | "real" | "bool" | "string" | "void" | identifier
     */
    private Type parseType() throws ParserException {
        if (lookahead.getToken() == VOID) {
            match(VOID);
            return new Type("void");
        }
        Symbol identifier = match(IDENTIFIER);
        if (lookahead.getToken() == SYMBOL_LEFT_BRACKET) {
            match(SYMBOL_LEFT_BRACKET);
            match(SYMBOL_RIGHT_BRACKET);
            return new Type(identifier.getLexeme() + "[]");
        }
        return new Type(identifier.getLexeme());
    }

    /**
     * Parses a general declaration
     * Grammar: GeneralDecl -> (const | var | val) Identifier Type = Expr ;
     * @return GeneralDecl - General declaration
     */
    private GeneralDecl parseGeneralDecl() throws ParserException {
        Lexer.Token name = lookahead.getToken();
        match(name);
        String identifier = match(IDENTIFIER).getLexeme();
        Type type = parseType();
        // If the next symbol is a semicolon, it is a declaration without assignment
        if (lookahead.getToken() == SYMBOL_SEMICOLON) {
            match(SYMBOL_SEMICOLON);
            return switch (name) {
                case KEYWORD_CONST -> new ConstDecl(type, identifier, null);
                case KEYWORD_VAR -> new VarDecl(type, identifier, null);
                case KEYWORD_VAL -> new ValDecl(type, identifier, null);
                default -> throw new ParserException("Expected a declaration but got " + lookahead.getToken());
            };
        }

        // Otherwise, it is a declaration with assignment
        match(SYMBOL_ASSIGN);
        Expr value;
        if (lookahead.getToken() == IDENTIFIER){
            // Array, record declaration or expression involving an identifier
            String n = type.getName();
            boolean isArray = n.contains("[]");
            String arrayType = isArray ? n.substring(0, n.length() - 2) : n;
            // If it is not an array, and the first letter is uppercase, it is a record declaration
            if (!isArray && Character.isUpperCase(arrayType.charAt(0))) {
                // Record declaration
                match(IDENTIFIER);
                value = parseRecordAssignment(n);
            } else if (isArray) {
                // In the case of an array declaration, the value between parenthesis is the length of the array
                match(IDENTIFIER);
                match(SYMBOL_LEFT_BRACKET);
                match(SYMBOL_RIGHT_BRACKET);
                match(SYMBOL_LEFT_PARENTHESIS);
                Expr initialCapacity = parseExpr(null);
                value = new ArrayExpr(new Type(arrayType), initialCapacity, new ArrayList<>());
                match(SYMBOL_RIGHT_PARENTHESIS);
            } else {
                // Expression involving an identifier
                value = parseExpr(new IdentifierExpr(n));
            }
        } else {
            value = parseExpr(null);
        }
        return switch (name) {
            case KEYWORD_CONST -> new ConstDecl(type, identifier, value);
            case KEYWORD_VAR -> new VarDecl(type, identifier, value);
            case KEYWORD_VAL -> new ValDecl(type, identifier, value);
            default -> throw new ParserException("Unexpected declaration type: " + name);
        };
    }

    /**
     * Parses an assignment
     * @return GeneralDecl - Assignment declaration object
     */
    private GeneralDecl parseAssignment() throws ParserException {
        String identifier = match(IDENTIFIER).getLexeme();
        Type type = null;
        for (GeneralDecl decl : program.getGlobalDecls()) {
            if (decl.getIdentifier().equals(identifier)) {
                type = decl.getType();
                break;
            }
            // Note : if no declaration is found, the type is null
        }
        match(SYMBOL_ASSIGN);
        Expr value = parseExpr(null);
        return new Assignment(type, identifier, value);
    }

    /**
     * Parses a procedure declaration
     * Grammar: ProcDecl -> identifier ( Params ) Type Block
     * @return ProcDecl - Procedure object
     */
    private ProcDecl parseProcDecl() throws ParserException {
        match(KEYWORD_PROC);
        String identifier = match(IDENTIFIER).getLexeme();
        match(SYMBOL_LEFT_PARENTHESIS);
        ArrayList<Param> params = parseParams();
        match(SYMBOL_RIGHT_PARENTHESIS);
        Type type = parseType();
        Block block = parseBlock();
        return new ProcDecl(identifier, params, type, block);
    }

    /**
     * Parses parameters
     * Grammar: Params -> e | Param MoreParams
     * @return ArrayList<Param> - List of parameters
     */
    private ArrayList<Param> parseParams() throws ParserException {
        ArrayList<Param> params = new ArrayList<>();
        if (lookahead.getToken() != SYMBOL_RIGHT_PARENTHESIS) {
            params.add(parseParam());
            while(lookahead.getToken() == SYMBOL_COMMA) {
                match(SYMBOL_COMMA);
                params.add(parseParam());
            }
        }
        return params;
    }

    /**
     * Parses a parameter
     * Grammar: Param -> identifier Type
     * @return Param - Parameter object
     */
    private Param parseParam() throws ParserException {
        String name = lookahead.getLexeme();
        match(IDENTIFIER);
        Type type = parseType();
        return new Param(name, type);
    }


    /**
     * Parses a Block structure
     * Grammar: Block -> "{" Stmts "}"
     * @return Block - Block object
     */
    private Block parseBlock() throws ParserException {
        match(SYMBOL_LEFT_BRACE);
        ArrayList<Object> statements = parseStmts();
        match(SYMBOL_RIGHT_BRACE);
        return new Block(statements);
    }


    /**
     * Parses statements
     * Grammar: Stmts -> Stmt Stmts | e
     * @return ArrayList<Stmt> - List of all the statements
     */
    private ArrayList<Object> parseStmts() throws ParserException {
        ArrayList<Object> statements = new ArrayList<>();
        while(lookahead.getToken() != SYMBOL_RIGHT_BRACE) {
            if (lookahead.getToken() == SYMBOL_SEMICOLON) {
                match(SYMBOL_SEMICOLON);
            } else {
                statements.add(parseStmt());
            }
        }
        return statements;
    }

    /**
     * Parses a statement
     * Grammar: Stmt -> IfElse | While | For | Assignment ";" | ProcCall ";"
     * @return Stmt - Statement object
     */
    private Object parseStmt() throws ParserException {
        switch (lookahead.getToken()) {
            case KEYWORD_IF -> {
                return parseIf();
            }
            case KEYWORD_ELSE -> {
                return parseElse();
            }
            case KEYWORD_WHILE -> {
                return parseWhile();
            }
            case KEYWORD_FOR -> {
                return parseFor();
            }
            case IDENTIFIER -> {
                return parseProcCall();
            }
            case KEYWORD_VAR, KEYWORD_VAL, KEYWORD_CONST -> {
                return parseGeneralDecl();
            }
            case KEYWORD_RETURN -> {
                return parseReturn();
            }
            case SYMBOL_SEMICOLON -> {
                match(SYMBOL_SEMICOLON);
                return parseStmt();
            }
            default -> throw new ParserException("Expected a statement but got " + lookahead.getToken());
        }
    }

    /**
     * Parses an if statement
     * Grammar: If -> "if" "(" Expr ")" Block
     * @return Stmt - Control structure object
     */
    private Stmt parseIf() throws ParserException {
        match(KEYWORD_IF);
        match(SYMBOL_LEFT_PARENTHESIS);
        Expr condition = parseExpr(null);
        match(SYMBOL_RIGHT_PARENTHESIS);
        Block body = parseBlock();
        return new CtrlStruct(KEYWORD_IF, condition, body);
    }

    /**
     * Parses an else statement
     * Grammar: Else -> "else" Block
     * @return Stmt - Control structure object
     */
    private Stmt parseElse() throws ParserException {
        match(KEYWORD_ELSE);
        match(SYMBOL_LEFT_PARENTHESIS);
        Expr condition = parseExpr(null);
        match(SYMBOL_RIGHT_PARENTHESIS);
        Block body = parseBlock();
        return new CtrlStruct(KEYWORD_ELSE, condition, body);
    }

    /**
     * Parses a while statement
     * Grammar: While -> Expression Block
     * @return Stmt - Control structure object
     */
    private Stmt parseWhile() throws ParserException {
        match(KEYWORD_WHILE);
        match(SYMBOL_LEFT_PARENTHESIS);
        Expr condition = parseExpr(null);
        match(SYMBOL_RIGHT_PARENTHESIS);
        Block body = parseBlock();
        return new CtrlStruct(KEYWORD_WHILE, condition, body);
    }

    /**
     * Parses a for statement
     * Grammar: For -> Expression "to" Expression "by" Expression Block
     * @return Stmt - Control structure object
     */
    private Stmt parseFor() throws ParserException {
        match(KEYWORD_FOR);
        Expr init = parseExpr(null);
        match(KEYWORD_TO);
        Expr end = parseExpr(null);
        match(KEYWORD_BY);
        Expr step = parseExpr(null);
        match(SYMBOL_RIGHT_PARENTHESIS);
        Block body = parseBlock();
        return new For(KEYWORD_FOR, init, end, step, body);
    }

    /**
     * Parses a procedure call or assignment statement
     * Grammar: ProcCall -> identifier "(" Exprs ")"
     * @return Stmt - Procedure call object
     */
    private Stmt parseProcCall() throws ParserException {
        String identifier = lookahead.getLexeme();
        match(IDENTIFIER);
        if (lookahead.getToken() == SYMBOL_LEFT_PARENTHESIS) {
            ArrayList<Expr> arguments = processBrackets(SYMBOL_LEFT_PARENTHESIS, SYMBOL_RIGHT_PARENTHESIS);
            match(SYMBOL_SEMICOLON);
            return new ProcCall(identifier, arguments);
        } else {
            match(SYMBOL_ASSIGN);
            Expr expr = parseExpr(null);
            match(SYMBOL_SEMICOLON);
            return new AssignmentStmt(identifier, expr);
        }
    }

    /**
     * Parses an expression
     * Grammar: Expr -> "(" Expr ")"
     *              | "-" Expr
     *              | "not" Expr
     *              | Expr ("*" | "/" | "%") Expr
     *              | Expr ("+" | "-") Expr
     *              | Expr ("<" | ">" | "<=" | ">=") Expr
     *              | Expr ("==" | "<>") Expr
     *              | Expr ("and" | "or") Expr
     *              | Integer
     *              | Real
     *              | Boolean
     *              | Strg
     *              | Array
     *              | Record
     *              | identifier
     * @param prec - Precedent expression
     * @return Expr - Expression object
     */
    private Expr parseExpr(Expr prec) throws ParserException {
        ArrayList<Lexer.Token> operators = new ArrayList<>(Arrays.asList(SYMBOL_MINUS, SYMBOL_PLUS, SYMBOL_MULTIPLY,
                SYMBOL_DIVIDE, SYMBOL_MODULO, SYMBOL_LESS_THAN, SYMBOL_GREATER_THAN, SYMBOL_LESS_THAN_OR_EQUAL,
                SYMBOL_GREATER_THAN_OR_EQUAL, SYMBOL_EQUAL, SYMBOL_NOT_EQUAL, KEYWORD_AND, KEYWORD_OR));
        switch (lookahead.getToken()) {
            case SYMBOL_LEFT_PARENTHESIS -> {
                match(SYMBOL_LEFT_PARENTHESIS);
                Expr expr = parseExpr(null);
                List<Lexer.Token> op = List.of(SYMBOL_MINUS, SYMBOL_PLUS, SYMBOL_MULTIPLY, SYMBOL_DIVIDE, SYMBOL_MODULO, SYMBOL_LESS_THAN, SYMBOL_GREATER_THAN, SYMBOL_LESS_THAN_OR_EQUAL, SYMBOL_GREATER_THAN_OR_EQUAL, SYMBOL_EQUAL);
                if (lookahead.getToken() == SYMBOL_RIGHT_PARENTHESIS) {
                    match(SYMBOL_RIGHT_PARENTHESIS);
                } else if (op.contains(lookahead.getToken())) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    expr = parseExpr(new BinaryExpr(expr, null, operator));
                }
                if (operators.contains(lookahead.getToken())) {
                    expr = parseExpr(expr);
                    if (lookahead.getToken() == SYMBOL_RIGHT_PARENTHESIS) {
                        match(SYMBOL_RIGHT_PARENTHESIS);
                    }
                }
                return expr;
            }
            case SYMBOL_MINUS, SYMBOL_PLUS, SYMBOL_MULTIPLY, SYMBOL_DIVIDE, SYMBOL_MODULO, SYMBOL_LESS_THAN, SYMBOL_GREATER_THAN, SYMBOL_LESS_THAN_OR_EQUAL, SYMBOL_GREATER_THAN_OR_EQUAL, SYMBOL_EQUAL, KEYWORD_AND, KEYWORD_OR -> {
                Lexer.Token operator = lookahead.getToken();
                match(operator);
                if (prec != null) {
                    return parseExpr(new BinaryExpr(prec, null, operator));
                } else {
                    return parseExpr(prec);
                }
            }
            case INTEGER -> {
                int value = Integer.parseInt(lookahead.getLexeme());
                match(INTEGER);
                if (prec != null) {
                    if (prec instanceof BinaryExpr) {
                        return parseExpr(new BinaryExpr(((BinaryExpr) prec).getLeft(), new IntegerExpr(value), ((BinaryExpr) prec).getOperator()));
                    } else {
                        return parseExpr(new BinaryExpr(prec, new IntegerExpr(value), lookahead.getToken()));
                    }
                } else if (lookahead.getToken() == SYMBOL_SEMICOLON || lookahead.getToken() == SYMBOL_RIGHT_PARENTHESIS){
                    return new IntegerExpr(value);
                } else if (operators.contains(lookahead.getToken())) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    return parseExpr(new BinaryExpr(new IntegerExpr(value), null, operator));
                } else {
                    return parseExpr(new IntegerExpr(value));
                }
            }
            case REAL -> {
                double value = Double.parseDouble(lookahead.getLexeme());
                match(REAL);
                if (prec != null) {
                    if (prec instanceof BinaryExpr) {
                        return parseExpr(new BinaryExpr(((BinaryExpr) prec).getLeft(), new RealExpr(value), ((BinaryExpr) prec).getOperator()));
                    } else {
                        return parseExpr(new BinaryExpr(prec, new RealExpr(value), lookahead.getToken()));
                    }
                } else if (lookahead.getToken() == SYMBOL_SEMICOLON){
                    return new RealExpr(value);
                } else if (operators.contains(lookahead.getToken())) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    return parseExpr(new BinaryExpr(new RealExpr(value), null, operator));
                } else {
                    return parseExpr(new RealExpr(value));
                }
            }
            case BOOLEAN -> {
                boolean value = Boolean.parseBoolean(lookahead.getLexeme());
                match(BOOLEAN);
                if (prec != null) {
                    if (prec instanceof BinaryExpr) {
                        return parseExpr(new BinaryExpr(((BinaryExpr) prec).getLeft(), new BooleanExpr(value), ((BinaryExpr) prec).getOperator()));
                    } else {
                        return parseExpr(new BinaryExpr(prec, new BooleanExpr(value), lookahead.getToken()));
                    }
                } else if (lookahead.getToken() == SYMBOL_SEMICOLON){
                    return new BooleanExpr(value);
                } else if (operators.contains(lookahead.getToken())) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    return parseExpr(new BinaryExpr(new BooleanExpr(value), null, operator));
                } else {
                    return parseExpr(new BooleanExpr(value));
                }
            }
            case STRING -> {
                String value = lookahead.getLexeme();
                match(STRING);
                if (prec != null) {
                    if (prec instanceof BinaryExpr) {
                        return parseExpr(new BinaryExpr(((BinaryExpr) prec).getLeft(), new StringExpr(value), ((BinaryExpr) prec).getOperator()));
                    } else {
                        return parseExpr(new BinaryExpr(prec, new StringExpr(value), lookahead.getToken()));
                    }
                } else if (lookahead.getToken() == SYMBOL_SEMICOLON){
                    return new StringExpr(value);
                } else if (operators.contains(lookahead.getToken())) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    return parseExpr(new BinaryExpr(new StringExpr(value), null, operator));
                } else {
                    return parseExpr(new StringExpr(value));
                }
            }
            case SYMBOL_LEFT_BRACKET -> {
                return parseArray(null);
            }
            case IDENTIFIER -> {
                Symbol id = match(IDENTIFIER);
                switch (lookahead.getToken()) {
                    case SYMBOL_MINUS, SYMBOL_PLUS, SYMBOL_MULTIPLY, SYMBOL_DIVIDE, SYMBOL_MODULO, SYMBOL_LESS_THAN, SYMBOL_GREATER_THAN, SYMBOL_LESS_THAN_OR_EQUAL, SYMBOL_GREATER_THAN_OR_EQUAL, SYMBOL_EQUAL, KEYWORD_AND, KEYWORD_OR -> {
                        return parseExpr(new IdentifierExpr(id.getLexeme()));
                    }
                    case SYMBOL_SEMICOLON -> {
                        if (prec != null) {
                            return parseExpr(new BinaryExpr(((BinaryExpr) prec).getLeft(), new IdentifierExpr(id.getLexeme()), ((BinaryExpr) prec).getOperator()));
                        } else {
                            return new IdentifierExpr(id.getLexeme());
                        }
                    }
                    case SYMBOL_LEFT_PARENTHESIS -> {
                        // Record declaration TODO: could be a function call
                        return parseRecordAssignment(id.getLexeme());
                    }
                }
                if (lookahead.getToken() == SYMBOL_SEMICOLON) {
                    return new IdentifierExpr(id.getLexeme());
                } else if (prec != null) {
                    Lexer.Token operator = lookahead.getToken();
                    if (operators.contains(operator)) {
                        match(operator);
                        Expr right = parseExpr(new IdentifierExpr(lookahead.getLexeme()));
                        return parseExpr(new BinaryExpr(prec, right, operator));
                    } else {
                        if (operator == SYMBOL_LEFT_BRACKET) {
                            match(SYMBOL_LEFT_BRACKET);
                            if (lookahead.getToken() == SYMBOL_RIGHT_BRACKET) {
                                // Array declaration
                                match(SYMBOL_RIGHT_BRACKET);
                                match(SYMBOL_LEFT_PARENTHESIS);
                                Expr initialCapacity = parseExpr(null);
                                Expr right = parseExpr(new ArrayExpr(new Type(id.getLexeme()), initialCapacity, new ArrayList<>()));
                                return parseExpr(new BinaryExpr(prec, right, operator));
                            } else {
                                // Array access
                                Expr index = parseExpr(null);
                                match(SYMBOL_RIGHT_BRACKET);
                                Expr right = parseExpr(new ArrayAccessExpr(new Type(id.getLexeme()), index));
                                return parseExpr(new BinaryExpr(prec, right, operator));
                            }
                        }
                    }
                } else if (lookahead.getToken() == SYMBOL_LEFT_BRACKET) {
                    // Either an array declaration or an array access
                    // In the case of an array declaration, the value between parenthesis is the length of the array
                    // and in the case of an array access, the value between brackets is the index of the array
                    match(SYMBOL_LEFT_BRACKET);
                    if (lookahead.getToken() == SYMBOL_RIGHT_BRACKET) {
                        // Array declaration
                        match(SYMBOL_RIGHT_BRACKET);
                        match(SYMBOL_LEFT_PARENTHESIS);
                        Expr initialCapacity = parseExpr(null);
                        return parseExpr(new ArrayExpr(new Type(id.getLexeme()), initialCapacity, new ArrayList<>()));
                    } else {
                        // Array access
                        Expr index = parseExpr(null);
                        match(SYMBOL_RIGHT_BRACKET);
                        return parseExpr(new ArrayAccessExpr(new Type(id.getLexeme()), index));
                    }
                } else {
                    return parseExpr(new IdentifierExpr(lookahead.getLexeme()));
                }
            }
            case SYMBOL_RIGHT_PARENTHESIS -> {
                match(SYMBOL_RIGHT_PARENTHESIS);
                return prec;
            }
            case SYMBOL_SEMICOLON, SYMBOL_COMMA, SYMBOL_RIGHT_BRACKET -> {
                return prec;
            }
            case SYMBOL_DOT -> {
                match(SYMBOL_DOT);
                String identifier = match(IDENTIFIER).getLexeme();
                Lexer.Token operator = lookahead.getToken();
                if (operators.contains(operator)) {
                    match(operator);
                    Expr right = parseExpr(null);
                    return parseExpr(new BinaryExpr(new RecordAccess(prec, new IdentifierExpr(identifier)), right, operator));
                }
                return parseExpr(new RecordAccess(prec, new IdentifierExpr(identifier)));
            }
            default -> throw new ParserException("Expected an expression but got " + lookahead.getToken());
        }
        return prec;
    }

    /**
     * Parses an array
     * Grammar: Array -> "[" Exprs "]"
     * @return Expr - Array object
     */
    private Expr parseArray(Type type) throws ParserException {
        if (type == null) {
            match(SYMBOL_LEFT_BRACKET);
            type = parseType();
            match(SYMBOL_RIGHT_BRACKET);
        }
        ArrayList<Expr> elements = processBrackets(SYMBOL_LEFT_BRACKET, SYMBOL_RIGHT_BRACKET);
        return new ArrayExpr(type, new Expr("int"), elements);
    }

    private ArrayList<Expr> processBrackets(Lexer.Token symbolLeftBracket, Lexer.Token symbolRightBracket) throws ParserException {
        match(symbolLeftBracket);
        ArrayList<Expr> elements = new ArrayList<>();
        while (lookahead.getToken() != symbolRightBracket) {
            if (lookahead.getToken() == INTEGER) {
                // Array access (e.g. a[0])
                elements.add(new IntegerExpr(Integer.parseInt(lookahead.getLexeme())));
                match(INTEGER);
            } else {
                elements.add(parseExpr(null));
                if (lookahead.getToken() == SYMBOL_COMMA) {
                    match(SYMBOL_COMMA);
                }
            }
        }
        match(symbolRightBracket);
        return elements;
    }

    /**
     * Parses a record
     * Grammar: Record -> record "(" Fields ")"
     * @return Expr - Record object
     */
    private Expr parseRecordAssignment(String record) throws ParserException {
        match(SYMBOL_LEFT_PARENTHESIS);
        RecordExpr newRecord = new RecordExpr(record, new ArrayList<>());
        int i = 0;
        while(lookahead.getToken() != SYMBOL_RIGHT_PARENTHESIS && lookahead.getToken() != SYMBOL_SEMICOLON) {
            newRecord.content.add(parseRecordEntry(i));
            if (lookahead.getToken() == SYMBOL_COMMA) {
                match(SYMBOL_COMMA);
            }
            i++;
        }
        if (lookahead.getToken() == SYMBOL_RIGHT_PARENTHESIS) {
            match(SYMBOL_RIGHT_PARENTHESIS);
        }
        return newRecord;
    }

    /**
     * Parses a record entry
     * @return RecordEntry - Record entry object
     */
    private RecordEntry parseRecordEntry(int index) throws ParserException {
        // Create a new record with the correct fields
        Expr content = parseExpr(null);
        return new RecordEntry(Integer.toString(index), content.getType().getName(), content);
    }

    /**
     * Find the type associated with a token
     * @param token - Token to find the type of
     * @return String representation of the type (e.g. "int")
     */
    private String findType(Lexer.Token token) {
        return switch (token) {
            case INTEGER -> "int";
            case REAL -> "real";
            case BOOLEAN -> "boolean";
            case STRING -> "string";
            default -> null;
        };
    }

    /**
     * Parses a return statement
     * Grammar: Return -> "return" Expr ";"
     * @return Stmt - Return object
     */
    private Stmt parseReturn() throws ParserException {
        match(KEYWORD_RETURN);
        Expr value = parseExpr(null);
        if (lookahead.getToken() == SYMBOL_SEMICOLON) {
            // Consume the semicolon if it was not consumed by the parseExpr method
            match(SYMBOL_SEMICOLON);
        }
        return new ReturnStmt(value);
    }

    /**
     * Returns the program obtained by the parsing
     */
    public Program getProgram() { return this.program; }
}
