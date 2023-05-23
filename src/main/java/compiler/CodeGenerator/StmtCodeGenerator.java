package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

public abstract class StmtCodeGenerator {
    public abstract void generateCode(MethodVisitor visitor);
}
