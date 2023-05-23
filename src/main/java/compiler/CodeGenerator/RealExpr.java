package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

public class RealExpr extends Expression {
    private final float value;

    public RealExpr(float value) {
        this.value = value;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        visitor.visitLdcInsn(value);
    }
}
