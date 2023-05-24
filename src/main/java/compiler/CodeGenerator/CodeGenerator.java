package compiler.CodeGenerator;
import compiler.Parser.Program;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

public class CodeGenerator {
    private ClassWriter writer;
    private MethodVisitor mv;

    public CodeGenerator(String className, String superClassName) {
        this.writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        // Replace "/" with "." in class and superclass names for internal ASM representation
        String internalClassName = className.replace('.', '/');
        String internalSuperClassName = (superClassName != null) ? superClassName.replace('.', '/') : "java/lang/Object";
        this.writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, internalClassName, null, internalSuperClassName, null);
        this.mv = writer.visitMethod(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null
        );
        mv.visitCode();
    }

    public byte[] generateByteCode(ProgramCodeGenerator program) {
        program.generateCode(this.writer, this.mv);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        writer.visitEnd();
        return writer.toByteArray();
    }
    public String generateCode(ProgramCodeGenerator program) {
        byte[] byteCode = this.generateByteCode(program);
        // Convert the bytecode into a readable format for displaying
        return Arrays.toString(byteCode);
    }
}