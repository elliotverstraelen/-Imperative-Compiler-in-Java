package compiler.CodeGenerator;

import compiler.Lexer.Lexer;
import compiler.Parser.Expr;
import jdk.incubator.vector.VectorOperators;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static jdk.incubator.vector.VectorOperators.*;

// Binary Expression CodeGen
public class BinaryExprCodeGenerator extends ExpressionCodeGenerator {
    protected final ExpressionCodeGenerator left;
    protected final ExpressionCodeGenerator right;
    protected final VectorOperators.Operator operator;

    public BinaryExprCodeGenerator(ExpressionCodeGenerator left, ExpressionCodeGenerator right, VectorOperators.Operator operator) {
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
            case SYMBOL_PLUS -> this.operator = ADD;
            case SYMBOL_MINUS -> this.operator = SUB;
            case SYMBOL_MULTIPLY -> this.operator = MUL;
            case SYMBOL_DIVIDE -> this.operator = DIV;
            default -> throw new RuntimeException("Invalid operator: " + operator);
        }
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        left.generateCode(visitor);
        right.generateCode(visitor);

        if (operator.equals(ADD)) {
            visitor.visitInsn(Opcodes.IADD);
        } else if (operator.equals(SUB)) {
            visitor.visitInsn(Opcodes.ISUB);
        } else if (operator.equals(MUL)) {
            visitor.visitInsn(Opcodes.IMUL);
        } else if (operator.equals(DIV)) {
            visitor.visitInsn(Opcodes.IDIV);
        }
    }

}

