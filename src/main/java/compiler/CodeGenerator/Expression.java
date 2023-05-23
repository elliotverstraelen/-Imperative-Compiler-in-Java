package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

// ... repeat for other classes like BinaryExprCodeGenerator, BlockCodeGenerator, etc.
public abstract class Expression {
    public abstract void generateCode(MethodVisitor visitor);
}
