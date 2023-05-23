package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class Program {
    private List<Declaration> declarations;

    public Program(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public byte[] generateCode() {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "Main", null, "java/lang/Object", null);
        for (Declaration decl : declarations) {
            decl.generateCode(writer, null);
        }
        writer.visitEnd();
        return writer.toByteArray();
    }
}
