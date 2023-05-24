package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ArrayExprCodeGenerator extends Expression {
    private Expression size;

    public ArrayExprCodeGenerator(Expression size) {
        this.size = size;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        size.generateCode(visitor);
        visitor.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
        // T_INT indicates that the elements of the array are of type int. We might need another code to do floats
    }
}
