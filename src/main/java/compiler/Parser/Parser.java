package compiler.Parser;
import compiler.Lexer.*;

import java.util.ArrayList;

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
        this.program = new Program();
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
                case KEYWORD_PROC -> this.program.add(parseProcDecl());
                case SYMBOL_SEMICOLON -> match(SYMBOL_SEMICOLON);
                case IDENTIFIER -> this.program.add(parseAssignment());
                default -> throw new ParserException("Expected a declaration but got " + lookahead.getToken());
            }
        }
    }

    /**
     * Parses a record declaration
     * Grammar: RecordDecl -> record Identifier { RecordFields } ;
     * @return RecordDecl - Record declaration
     */
    private RecordT parseRecordDecl() throws ParserException {
        match(KEYWORD_RECORD);
        Symbol identifier = match(IDENTIFIER);
        match(SYMBOL_LEFT_BRACE);
        ArrayList<RecordEntry> recordFields = parseRecordFields();
        match(SYMBOL_RIGHT_BRACE);
        return new RecordT(identifier.getLexeme(), recordFields);
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
        match(SYMBOL_ASSIGN);
        Expr value;
        if (lookahead.getToken() == IDENTIFIER){
            // Array or record declaration
            String n = parseType().getName();
            boolean isArray = n.contains("[]");
            if (!isArray && lookahead.getToken() == SYMBOL_LEFT_PARENTHESIS) {
                // Record declaration
                value = parseRecordAssignment(n);
            } else if (isArray) {
                // In the case of an array declaration, the value between brackets is the length of the array
                match(SYMBOL_LEFT_PARENTHESIS);
                int initialCapacity = Integer.parseInt(match(INTEGER).getLexeme());
                String arrayType = type.getName().substring(0, type.getName().length() - 2);
                value = new ArrayExpr(new Type(arrayType), new ArrayList<>(initialCapacity));
                match(SYMBOL_RIGHT_PARENTHESIS);
            } else {
                value = parseExpr(null);
            }
        } else {
            value = parseExpr(null);
        }
        GeneralDecl generalDecl = switch (name) {
            case KEYWORD_CONST -> new ConstDecl(type, identifier, value);
            case KEYWORD_VAR -> new VarDecl(type, identifier, value);
            case KEYWORD_VAL -> new ValDecl(type, identifier, value);
            default -> throw new ParserException("Unexpected declaration type: " + name);
        };
        return generalDecl;
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
        ProcDecl procDecl = new ProcDecl(type, identifier, params, block);
        return procDecl;
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
        ArrayList<Stmt> statements = parseStmts();
        match(SYMBOL_RIGHT_BRACE);
        return new Block(statements);
    }


    /**
     * Parses statements
     * Grammar: Stmts -> Stmt Stmts | e
     * @return ArrayList<Stmt> - List of all the statements
     */
    private ArrayList<Stmt> parseStmts() throws ParserException {
        ArrayList<Stmt> statements = new ArrayList<>();
        while(lookahead.getToken() != SYMBOL_RIGHT_BRACE) {
            statements.add(parseStmt());
        }
        return statements;
    }

    /**
     * Parses a statement
     * Grammar: Stmt -> IfElse | While | For | Assignment ";" | ProcCall ";"
     * @return Stmt - Statement object
     */
    private Stmt parseStmt() throws ParserException {
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
                match(IDENTIFIER);
                if (lookahead.getToken() == SYMBOL_LEFT_PARENTHESIS) {
                    return parseProcCall();
                } else {
                    return parseAssignment();
                }
            }
            case KEYWORD_RETURN -> {
                return parseReturn();
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
     * Parses an assignment
     * Grammar: Assignment -> Left "=" Expr
     * @return Stmt - Assignment object
     */
    private Stmt parseAssignment() throws ParserException {
        Object left = parseLeft();
        match(SYMBOL_ASSIGN);
        Expr value = parseExpr(null);
        match(SYMBOL_SEMICOLON);
        return new Assignment(SYMBOL_ASSIGN, left, value);
    }

    /**
     * Parses the left part of an assignment
     * Grammar: identifier | Left "[" Expr "]" | Left "." identifier
     * @return Object - Left part of an assignment TODO: precise type
     */
    private Stmt parseLeft() throws ParserException {
        switch (lookahead.getToken()) {
            case IDENTIFIER -> {
                String varName = lookahead.getLexeme();
                match(IDENTIFIER);
                return new Left(IDENTIFIER, varName);
            }
            case SYMBOL_LEFT_BRACKET -> {
                // Array access
                match(SYMBOL_LEFT_BRACKET);
                Expr index = parseExpr(null);
                match(SYMBOL_RIGHT_BRACKET);
                return new ArrayAccess(ARRAY, null, index); // Identifier is null because as it is supposed to be the closest identifier in the AST
            }
            case SYMBOL_DOT -> {
                // Field access
                match(SYMBOL_DOT);
                String field = lookahead.getLexeme();
                match(IDENTIFIER);
                return new RecordAccess(RECORD, null, field);
            }
            default -> throw new ParserException("Expected an identifier, a left bracket or a dot but got " + lookahead.getToken());
        }
    }

    /**
     * Parses a procedure call
     * Grammar: ProcCall -> identifier "(" Exprs ")"
     * @return Stmt - Procedure call object
     */
    private Stmt parseProcCall() throws ParserException {
        String identifier = lookahead.getLexeme();
        match(IDENTIFIER);
        ArrayList<Expr> arguments = processBrackets(SYMBOL_LEFT_PARENTHESIS, SYMBOL_RIGHT_PARENTHESIS);
        match(SYMBOL_SEMICOLON);
        return new ProcCall(identifier, arguments);
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
        switch (lookahead.getToken()) {
            case SYMBOL_LEFT_PARENTHESIS -> {
                match(SYMBOL_LEFT_PARENTHESIS);
                Expr expr = parseExpr(null);
                if (lookahead.getToken() == SYMBOL_RIGHT_PARENTHESIS) {
                    match(SYMBOL_RIGHT_PARENTHESIS);
                }
                return expr;
            }
            case SYMBOL_MINUS, SYMBOL_PLUS, SYMBOL_MULTIPLY, SYMBOL_DIVIDE, SYMBOL_MODULO, SYMBOL_LESS_THAN, SYMBOL_GREATER_THAN, SYMBOL_LESS_THAN_OR_EQUAL, SYMBOL_GREATER_THAN_OR_EQUAL, SYMBOL_EQUAL, KEYWORD_AND, KEYWORD_OR -> {
                Lexer.Token operator = lookahead.getToken();
                match(operator);
                Expr right = parseExpr(prec);
                return new BinaryExpr(prec, right, operator);
            }
            case INTEGER -> {
                int value = Integer.parseInt(lookahead.getLexeme());
                match(INTEGER);
                if (lookahead.getToken() == SYMBOL_SEMICOLON){
                    return new IntegerExpr(value);
                } else if (prec != null) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    Expr right = parseExpr(new IntegerExpr(value));
                    return parseExpr(new BinaryExpr(prec, right, operator));
                } else {
                    return parseExpr(new IntegerExpr(value));
                }
            }
            case REAL -> {
                double value = Double.parseDouble(lookahead.getLexeme());
                match(REAL);
                if (lookahead.getToken() == SYMBOL_SEMICOLON){
                    return new RealExpr(value);
                } else if (prec != null) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    Expr right = parseExpr(new RealExpr(value));
                    return parseExpr(new BinaryExpr(prec, right, operator));
                } else {
                    return parseExpr(new RealExpr(value));
                }
            }
            case BOOLEAN -> {
                boolean value = Boolean.parseBoolean(lookahead.getLexeme());
                match(BOOLEAN);
                if (lookahead.getToken() == SYMBOL_SEMICOLON){
                    return new BooleanExpr(value);
                } else if (prec != null) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    Expr right = parseExpr(new BooleanExpr(value));
                    return parseExpr(new BinaryExpr(prec, right, operator));
                } else {
                    return parseExpr(new BooleanExpr(value));
                }
            }
            case STRING -> {
                String value = lookahead.getLexeme();
                match(STRING);
                if (lookahead.getToken() == SYMBOL_SEMICOLON){
                    return new StringExpr(value);
                } else if (prec != null) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    Expr right = parseExpr(new StringExpr(value));
                    return parseExpr(new BinaryExpr(prec, right, operator));
                } else {
                    return parseExpr(new StringExpr(value));
                }
            }
            case SYMBOL_LEFT_BRACKET -> {
                return parseArray(null);
            }
            case IDENTIFIER -> {
                Symbol id = match(IDENTIFIER);
                switch (lookahead.getToken()){
                    case SYMBOL_MINUS, SYMBOL_PLUS, SYMBOL_MULTIPLY, SYMBOL_DIVIDE, SYMBOL_MODULO, SYMBOL_LESS_THAN, SYMBOL_GREATER_THAN, SYMBOL_LESS_THAN_OR_EQUAL, SYMBOL_GREATER_THAN_OR_EQUAL, SYMBOL_EQUAL, KEYWORD_AND, KEYWORD_OR -> {
                        return parseExpr(new IdentifierExpr(id.getLexeme()));
                    }
                }
                if (lookahead.getToken() == SYMBOL_SEMICOLON){
                    return new IdentifierExpr(lookahead.getLexeme());
                } else if (prec != null) {
                    Lexer.Token operator = lookahead.getToken();
                    match(operator);
                    Expr right = parseExpr(new IdentifierExpr(lookahead.getLexeme()));
                    return parseExpr(new BinaryExpr(prec, right, operator));
                } else {
                    return parseExpr(new IdentifierExpr(lookahead.getLexeme()));
                }
            }
            case SYMBOL_RIGHT_PARENTHESIS -> {
                match(SYMBOL_RIGHT_PARENTHESIS);
                return prec;
            }
            case SYMBOL_SEMICOLON -> {
                return prec;
            }
            default -> throw new ParserException("Expected an expression but got " + lookahead.getToken());
        }
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
        return new ArrayExpr(type, elements);
    }

    private ArrayList<Expr> processBrackets(Lexer.Token symbolLeftBracket, Lexer.Token symbolRightBracket) throws ParserException {
        match(symbolLeftBracket);
        ArrayList<Expr> elements = new ArrayList<>();
        while (lookahead.getToken() != symbolRightBracket) {
            elements.add(parseExpr(null));
            if (lookahead.getToken() == SYMBOL_COMMA) {
                match(SYMBOL_COMMA);
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
        while(lookahead.getToken() != SYMBOL_RIGHT_PARENTHESIS) {
            // Create a new record with the correct fields
            String value = lookahead.getLexeme();
            String type = findType(lookahead.getToken());
            Expr subrecord = null;
            if (type == null && !value.equals("int") && !value.equals("real") && !value.equals("boolean") && !value.equals("string") && !value.equals("[") && !value.equals("]")) {
                // Could be a record
                match(IDENTIFIER);
                type = value;
                subrecord = parseRecordAssignment(value);
            }
            // Put the value in the correct field (should be in order)
            newRecord.content.add(new RecordEntry(value, type, subrecord));
            match(lookahead.getToken());
            if (lookahead.getToken() == SYMBOL_COMMA) {
                match(SYMBOL_COMMA);
            }
        }
        match(SYMBOL_RIGHT_PARENTHESIS);
        return newRecord;
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
        match(SYMBOL_SEMICOLON);
        return new ReturnStmt(value);
    }

    /**
     * Returns the program obtained by the parsing
     */
    public Program getProgram() { return this.program; }
}
