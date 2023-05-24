package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class BlockCodeGenerator extends StmtCodeGenerator {
    private final List<StmtCodeGenerator> stmts;

    public BlockCodeGenerator(List<StmtCodeGenerator> stmts) {
        this.stmts = stmts;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        for (StmtCodeGenerator stmt : stmts) {
            stmt.generateCode(visitor);
        }
    }
}
