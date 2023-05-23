package compiler.CodeGenerator;
import compiler.Parser.Program;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

public class CodeGenerator {
    private ClassWriter writer;

    public CodeGenerator() {
        this.writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    }

    public byte[] generateByteCode(ProgramCodeGenerator program) {
        MethodVisitor mv = writer.visitMethod(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null
        );
        program.generateCode(this.writer, mv);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        writer.visitEnd();
        return writer.toByteArray();
    }

    public String generateCode(ProgramCodeGenerator program) {
        byte[] byteCode = this.generateByteCode(program);
        // Convert the bytecode into a readable format for displaying
        return Arrays.toString(byteCode); // Placeholder for now
    }
}