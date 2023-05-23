package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

public abstract class ControlStructure {
    public abstract void generateCode(MethodVisitor visitor);
}
