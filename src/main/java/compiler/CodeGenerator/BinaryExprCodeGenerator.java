package compiler.CodeGenerator;

import compiler.Lexer.Lexer;
import compiler.Parser.Expr;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

// Binary Expression CodeGen
public class BinaryExprCodeGenerator extends ExpressionCodeGenerator {
    protected final ExpressionCodeGenerator left;
    protected final ExpressionCodeGenerator right;
    protected final int operator; // Opcodes.IADD, Opcodes.ISUB, Opcodes.IMUL, Opcodes.IDIV, etc.

    public BinaryExprCodeGenerator(ExpressionCodeGenerator left, ExpressionCodeGenerator right, int operator) {
        super("BinaryExpr");
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public BinaryExprCodeGenerator(Expr left, Expr right, Lexer.Token operator) {
        super("BinaryExpr");
        this.left = castExpr(left);
        this.right = castExpr(right);
        switch (operator) {
            case SYMBOL_PLUS -> this.operator = Opcodes.IADD;
            case SYMBOL_MINUS -> this.operator = Opcodes.ISUB;
            case SYMBOL_MULTIPLY -> this.operator = Opcodes.IMUL;
            case SYMBOL_DIVIDE -> this.operator = Opcodes.IDIV;
            default -> throw new RuntimeException("Invalid operator: " + operator);
        }
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        left.generateCode(visitor);
        right.generateCode(visitor);
        visitor.visitInsn(operator);
    }

}

