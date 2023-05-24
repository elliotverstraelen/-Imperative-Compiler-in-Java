package compiler.CodeGenerator;

import compiler.Lexer.Lexer;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class ForCodeGenerator extends CtrlStructCodeGenerator {
    private ExpressionCodeGenerator step;

    public ForCodeGenerator(Lexer.Token name, ExpressionCodeGenerator condition, BlockCodeGenerator body, ExpressionCodeGenerator step) {
        super(Lexer.Token.KEYWORD_FOR, condition, body);
        this.step = step;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        Label loopStart = new Label();
        visitor.visitLabel(loopStart);
        condition.generateCode(visitor);
        Label loopEnd = new Label();
        visitor.visitJumpInsn(Opcodes.IFEQ, loopEnd);
        this.thenBody.generateCode(visitor);
        step.generateCode(visitor);
        visitor.visitJumpInsn(Opcodes.GOTO, loopStart);
        visitor.visitLabel(loopEnd);
    }
}
