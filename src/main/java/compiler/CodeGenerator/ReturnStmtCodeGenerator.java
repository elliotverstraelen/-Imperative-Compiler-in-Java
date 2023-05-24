package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ReturnStmtCodeGenerator extends StmtCodeGenerator {
    private ExpressionCodeGenerator expr;

    public ReturnStmtCodeGenerator(ExpressionCodeGenerator expr) {
        this.expr = expr;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        if (expr != null) {
            expr.generateCode(visitor);
            visitor.visitInsn(Opcodes.ARETURN);
        } else {
            visitor.visitInsn(Opcodes.RETURN);
        }
    }
}
