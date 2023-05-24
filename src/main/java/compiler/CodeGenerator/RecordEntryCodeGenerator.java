package compiler.CodeGenerator;

import compiler.Parser.Type;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class RecordEntryCodeGenerator {
    private final String name;
    private final Type type;
    private final ExpressionCodeGenerator value;

    public RecordEntryCodeGenerator(String name, Type type, ExpressionCodeGenerator value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        String descriptor = type.getName();
        writer.visitField(Opcodes.ACC_PUBLIC, name, descriptor, null, null).visitEnd();
    }
}
