package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Assignment extends StmtCodeGenerator {
    private IdentifierExpr identifier;
    private Expression expression;

    public Assignment(IdentifierExpr identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        expression.generateCode(visitor);
        identifier.generateCode(visitor);
        visitor.visitFieldInsn(Opcodes.PUTSTATIC, "MyClass", identifier.getName(), "I");
    }
}
