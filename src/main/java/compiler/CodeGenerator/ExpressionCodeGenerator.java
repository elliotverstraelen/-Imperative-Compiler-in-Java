package compiler.CodeGenerator;

import compiler.Parser.*;
import org.objectweb.asm.MethodVisitor;

public abstract class ExpressionCodeGenerator {
    protected final String name;

    protected ExpressionCodeGenerator(String name) { this.name = name; }

    public static ExpressionCodeGenerator castExpr(Expr expr) {
        if (expr instanceof IntegerExpr integerExpr) {
            return new IntegerExprCodeGenerator(integerExpr.getValue());
        } else if (expr instanceof RealExpr realExpr) {
            return new RealExprCodeGenerator((float) realExpr.getValue());
        } else if (expr instanceof BooleanExpr booleanExpr) {
            return new BooleanExprCodeGenerator(booleanExpr.getValue());
        } else if (expr instanceof StringExpr stringExpr) {
            return new StringExprCodeGenerator(stringExpr.getValue());
        } else if (expr instanceof ArrayExpr arrayExpr) {
            return new ArrayExprCodeGenerator(arrayExpr.getType(), arrayExpr.getSize(), arrayExpr.getContent());
        } else if (expr instanceof BinaryExpr binaryExpr) {
            return new BinaryExprCodeGenerator(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator());
        } else if (expr instanceof IdentifierExpr identifierExpr) {
            return new IdentifierExprCodeGenerator(identifierExpr.getIdentifier());
        } else if (expr instanceof RecordExpr recordExpr) {
            return new RecordExprCodeGenerator(recordExpr.getType().getName(), recordExpr.getContent());
        } else {
            throw new IllegalStateException("Unexpected expression type: " + expr.getType());
        }
    }

    public abstract void generateCode(MethodVisitor visitor);

    public String getName() { return name; }
}
