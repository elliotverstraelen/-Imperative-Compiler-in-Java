package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class IntegerExprCodeGenerator extends ExpressionCodeGenerator {
    private final int value;

    public IntegerExprCodeGenerator(int value) {
        this.value = value;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        visitor.visitIntInsn(Opcodes.BIPUSH, value);
    }
}
