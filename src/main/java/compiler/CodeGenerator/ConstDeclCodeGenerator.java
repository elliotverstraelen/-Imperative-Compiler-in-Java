package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ConstDeclCodeGenerator extends Declaration {
    private final String identifier;
    private final int value;

    public ConstDeclCodeGenerator(String identifier, int value) {
        this.identifier = identifier;
        this.value = value;
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor visitor) {
        FieldVisitor fv = writer.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, identifier, "I", null, value);
        fv.visitEnd();
    }
}
