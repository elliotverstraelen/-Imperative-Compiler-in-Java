package compiler.CodeGenerator;

import compiler.Parser.Expr;
import compiler.Parser.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;

public class ArrayExprCodeGenerator extends ExpressionCodeGenerator {
    protected final Type type; // Type of the elements of the array
    protected final ExpressionCodeGenerator size; // Size of the array
    protected final ArrayList<ExpressionCodeGenerator> content; // Content of the array

    public ArrayExprCodeGenerator(Type type, ExpressionCodeGenerator size, ArrayList<ExpressionCodeGenerator> content) {
        super("ArrayExpr");
        this.type = type;
        this.size = size;
        this.content = content;
    }

    public ArrayExprCodeGenerator(Type type, Expr size, ArrayList<Expr> content) {
        super("ArrayExpr");
        this.type = type;
        this.size = castExpr(size);
        this.content = new ArrayList<>();
        for (Expr e : content) {
            this.content.add(castExpr(e));
        }
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        size.generateCode(visitor);
        visitor.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
        // T_INT indicates that the elements of the array are of type int.
    }
}
