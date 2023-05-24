package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ArrayAccessCodeGenerator extends Expression {
    private Expression arrayRef;
    private Expression index;

    public ArrayAccessCodeGenerator(Expression arrayRef, Expression index) {
        this.arrayRef = arrayRef;
        this.index = index;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        // generate bytecode for array reference
        arrayRef.generateCode(visitor);
        // generate bytecode for index
        index.generateCode(visitor);
        // load the array element at the given index
        visitor.visitInsn(Opcodes.IALOAD);
    }
}
/**
 * Note: IALOAD is used for arrays of integers. We might need to implement floats and use another code
 */