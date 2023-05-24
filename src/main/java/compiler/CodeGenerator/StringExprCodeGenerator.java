package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

public class StringExprCodeGenerator extends ExpressionCodeGenerator {
    private final String value;

    public StringExprCodeGenerator(String value) {
        this.value = value;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        visitor.visitLdcInsn(value);
    }
}
