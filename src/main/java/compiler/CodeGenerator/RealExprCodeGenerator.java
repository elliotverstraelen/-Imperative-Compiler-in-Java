package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

public class RealExprCodeGenerator extends ExpressionCodeGenerator {
    private final float value;

    public RealExprCodeGenerator(float value) {
        super("RealExpr");
        this.value = value;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        visitor.visitLdcInsn(value);
    }
}
