package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class IntegerConstant extends Expression {
    private final int value;

    public IntegerConstant(int value) {
        this.value = value;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        visitor.visitIntInsn(Opcodes.BIPUSH, value);
    }
}
