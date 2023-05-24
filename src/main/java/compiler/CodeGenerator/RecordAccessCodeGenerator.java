package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class RecordAccessCodeGenerator extends ExpressionCodeGenerator {
    private final String name;

    public RecordAccessCodeGenerator(String name) {
        this.name = name;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        // Assuming all variables are global and stored as static fields
        visitor.visitFieldInsn(Opcodes.GETSTATIC, "MyClass", name, "I");
    }
}
