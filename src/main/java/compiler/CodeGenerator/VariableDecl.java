package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class VariableDecl extends Declaration {
    private String name;
    private Expression initExpr;

    public VariableDecl(String name, Expression initExpr) {
        this.name = name;
        this.initExpr = initExpr;
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        mv.visitFieldInsn(Opcodes.ACC_PUBLIC, name, "I", null);
        if (initExpr != null) {
            initExpr.generateCode(mv);
            mv.visitFieldInsn(Opcodes.PUTSTATIC, String.valueOf(writer.getClass()), name, "I");
        }
    }
}
