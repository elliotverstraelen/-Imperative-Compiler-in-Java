package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MultiplicationExpr extends Expression {
    private final Expression left, right;

    public MultiplicationExpr(Expression left, Expression right) {
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
