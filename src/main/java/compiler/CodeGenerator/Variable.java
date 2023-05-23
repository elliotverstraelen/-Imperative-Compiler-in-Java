package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        // Assuming all variables are global and stored as static fields
        visitor.visitFieldInsn(Opcodes.GETSTATIC, "MyClass", name, "I");
    }

    public String getName() {
        return this.name;
    }
}
