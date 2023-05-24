package compiler.CodeGenerator;

import compiler.Parser.Param;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.List;

public class ProcDeclCodeGenerator extends DeclarationCodeGenerator {
    private String name;
    private List<Param> params;
    private List<StmtCodeGenerator> body;

    public ProcDeclCodeGenerator(String name, List<Param> params, List<StmtCodeGenerator> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public void generateCode(ClassWriter writer) {
        String descriptor = MethodType.methodType(void.class, Collections.nCopies(params.size(), int.class)).toMethodDescriptorString();
        MethodVisitor mv = writer.visitMethod(Opcodes.ACC_STATIC, name, descriptor, null, null);
        for (StmtCodeGenerator stmt : body) {
            stmt.generateCode(mv);
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(-1, -1); // Compute maximum stack and local variable size automatically
        mv.visitEnd();
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        // You may need to provide your own implementation here.
        // Following is just a stub implementation

        for (StmtCodeGenerator stmt : body) {
            stmt.generateCode(mv);
        }
        mv.visitInsn(Opcodes.RETURN);
    }
}
