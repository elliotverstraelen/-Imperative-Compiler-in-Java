package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

public class StringExpr extends Expression {
    private final String value;

    public StringExpr(String value) {
        this.value = value;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        visitor.visitLdcInsn(value);
    }
}
