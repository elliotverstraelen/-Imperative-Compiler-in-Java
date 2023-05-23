package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class RecordEntryCodeGenerator {
    private String name;
    private Type type;

    public RecordEntryCodeGenerator(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        String descriptor = type.toDescriptor();
        writer.visitField(Opcodes.ACC_PUBLIC, name, descriptor, null, null).visitEnd();
    }
}
