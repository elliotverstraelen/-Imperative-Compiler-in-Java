package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class IdentifierExprCodeGenerator extends ExpressionCodeGenerator {
    private final String identifier;

    public IdentifierExprCodeGenerator(String identifier) {
        super("IdentifierExpr");
        this.identifier = identifier;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        // Assuming all variables are global and stored as static fields
        visitor.visitFieldInsn(Opcodes.GETSTATIC, "MyClass", identifier, "I");
    }

    public String getName() {
        return this.identifier;
    }
}
