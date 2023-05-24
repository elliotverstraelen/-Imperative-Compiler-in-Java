package compiler.CodeGenerator;

import compiler.Lexer.Lexer;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ArrayStmtCodeGenerator extends StmtCodeGenerator {
    private String arrayRef;
    private ExpressionCodeGenerator index;

    public ArrayStmtCodeGenerator(String arrayRef, ExpressionCodeGenerator index) {
        super(Lexer.Token.SYMBOL_ASSIGN);
        this.arrayRef = arrayRef;
        this.index = index;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        index.generateCode(visitor);
        visitor.visitFieldInsn(Opcodes.PUTSTATIC, "MyClass", index.getName(), "I");
    }
}
