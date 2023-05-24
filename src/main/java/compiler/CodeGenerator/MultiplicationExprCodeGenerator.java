package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MultiplicationExprCodeGenerator extends ExpressionCodeGenerator {
    private final ExpressionCodeGenerator left, right;

    public MultiplicationExprCodeGenerator(ExpressionCodeGenerator left, ExpressionCodeGenerator right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        left.generateCode(visitor);
        right.generateCode(visitor);
        visitor.visitInsn(Opcodes.IMUL);
    }
}
