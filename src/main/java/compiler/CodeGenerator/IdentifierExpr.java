package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class IdentifierExpr extends Expression {
    private final String identifier;

    public IdentifierExpr(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        // Assuming all variables are global and stored as static fields
        visitor.visitFieldInsn(Opcodes.GETSTATIC, "MyClass", identifier, "I");
    }
}
