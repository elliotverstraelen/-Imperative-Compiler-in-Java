package compiler.CodeGenerator;

import compiler.Lexer.Lexer;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AssignmentCodeGenerator extends StmtCodeGenerator {
    private final IdentifierExprCodeGenerator identifier;
    private final ExpressionCodeGenerator expression;

    public AssignmentCodeGenerator(IdentifierExprCodeGenerator identifier, ExpressionCodeGenerator expression) {
        super(Lexer.Token.SYMBOL_ASSIGN);
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
