package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class Block extends StmtCodeGenerator {
    private final List<StmtCodeGenerator> stmts;

    public Block(List<StmtCodeGenerator> stmts) {
        this.stmts = stmts;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        for (StmtCodeGenerator stmt : stmts) {
            stmt.generateCode(visitor);
        }
    }
}
