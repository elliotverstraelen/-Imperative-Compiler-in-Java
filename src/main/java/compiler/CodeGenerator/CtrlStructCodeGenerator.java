package compiler.CodeGenerator;

import compiler.Lexer.Lexer;
import compiler.Parser.Stmt;

public abstract class CtrlStructCodeGenerator extends StmtCodeGenerator {

    // This is an abstract class, it will not have an implementation of generateCode()
    // Specific control structures (like if statements, loops etc.) will provide their own implementation
}
