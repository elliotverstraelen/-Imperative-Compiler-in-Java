package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public abstract class Declaration {
    public abstract void generateCode(ClassWriter writer, MethodVisitor visitor);
}
