package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class RecordDeclCodeGenerator extends DeclarationCodeGenerator {
    private String name;
    private List<RecordEntryCodeGenerator> entries;

    public RecordDeclCodeGenerator(String name, List<RecordEntryCodeGenerator> entries) {
        this.name = name;
        this.entries = entries;
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        ClassWriter recordWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        recordWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, name, null, "java/lang/Object", null);

        for (RecordEntryCodeGenerator entry : entries) {
            entry.generateCode(recordWriter, null);
        }

        recordWriter.visitEnd();
        byte[] recordClass = recordWriter.toByteArray();
    }
}
