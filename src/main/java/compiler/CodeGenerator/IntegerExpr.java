package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class IntegerExpr extends Expression {
    private final int value;

    public IntegerExpr(int value) {
        this.value = value;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        visitor.visitIntInsn(Opcodes.BIPUSH, value);
    }
}
