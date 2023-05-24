package compiler.CodeGenerator;

import jdk.incubator.vector.VectorOperators;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static jdk.incubator.vector.VectorOperators.*;

// Binary Expression CodeGen
public class BinaryExprCodeGenerator extends Expression {
    private Expression left, right;
    private VectorOperators.Operator operator;

    public BinaryExprCodeGenerator(Expression left, Expression right, VectorOperators.Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        left.generateCode(visitor);
        right.generateCode(visitor);

        if (operator.equals(ADD)) {
            visitor.visitInsn(Opcodes.IADD);
        } else if (operator.equals(SUB)) {
            visitor.visitInsn(Opcodes.ISUB);
        } else if (operator.equals(MUL)) {
            visitor.visitInsn(Opcodes.IMUL);
        } else if (operator.equals(DIV)) {
            visitor.visitInsn(Opcodes.IDIV);
            // other operands
        }
    }

}

