package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

// Boolean Expression CodeGen
public class BooleanExpr extends Expression {
    private boolean value;

    public BooleanExpr(boolean value) {
        this.value = value;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        // Push the integer constant 0 (for false) or 1 (for true) onto the operand stack
        visitor.visitInsn(value ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
    }
}
