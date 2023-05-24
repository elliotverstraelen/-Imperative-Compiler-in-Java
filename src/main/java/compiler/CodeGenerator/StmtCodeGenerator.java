package compiler.CodeGenerator;

import compiler.Lexer.Lexer;
import org.objectweb.asm.MethodVisitor;

public abstract class StmtCodeGenerator {
    protected final Lexer.Token name;
    public StmtCodeGenerator(Lexer.Token name) {
        this.name = name;
    }

    public abstract void generateCode(MethodVisitor visitor);
}
