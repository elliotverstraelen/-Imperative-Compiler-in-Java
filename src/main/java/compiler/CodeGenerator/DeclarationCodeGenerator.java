package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public abstract class DeclarationCodeGenerator {
    public abstract void generateCode(ClassWriter writer, MethodVisitor visitor);
}
