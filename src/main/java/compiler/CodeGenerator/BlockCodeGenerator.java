package compiler.CodeGenerator;

import compiler.Lexer.Lexer;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class BlockCodeGenerator extends StmtCodeGenerator {
    private final List<StmtCodeGenerator> stmts;

    public BlockCodeGenerator(List<StmtCodeGenerator> stmts) {
        super(Lexer.Token.UNKNOWN);
        this.stmts = stmts;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        for (StmtCodeGenerator stmt : stmts) {
            stmt.generateCode(visitor);
        }
    }
}
