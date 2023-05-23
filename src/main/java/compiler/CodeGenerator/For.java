package compiler.CodeGenerator;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class For extends CtrlStruct {
    private StmtCodeGenerator initialization;
    private Expression condition;
    private StmtCodeGenerator increment;
    private List<StmtCodeGenerator> body;

    public For(StmtCodeGenerator initialization, Expression condition, StmtCodeGenerator increment, List<StmtCodeGenerator> body) {
        this.initialization = initialization;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        initialization.generateCode(visitor);
        Label loopStart = new Label();
        visitor.visitLabel(loopStart);
        condition.generateCode(visitor);
        Label loopEnd = new Label();
        visitor.visitJumpInsn(Opcodes.IFEQ, loopEnd);
        for (StmtCodeGenerator stmt : body) {
            stmt.generateCode(visitor);
        }
        increment.generateCode(visitor);
        visitor.visitJumpInsn(Opcodes.GOTO, loopStart);
        visitor.visitLabel(loopEnd);
    }
}
