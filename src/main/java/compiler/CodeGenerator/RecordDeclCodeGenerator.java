package compiler.CodeGenerator;

import compiler.Parser.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.util.ArrayList;

public class RecordDeclCodeGenerator extends GeneralDeclCodeGenerator {
    private final ArrayList<RecordEntryCodeGenerator> fields;

    public RecordDeclCodeGenerator(String name, ArrayList<RecordEntry> fields) {
        super(name, null, "R");
        this.fields = new ArrayList<>();
        for (RecordEntry field : fields) {
            Expr expression = field.getValue();
            if (expression instanceof IntegerExpr integerExpr) {
                this.fields.add(new RecordEntryCodeGenerator(field.getIdentifier(), field.getType(), new IntegerExprCodeGenerator(integerExpr.getValue())));
            } else if (expression instanceof RealExpr realExpr) {
                this.fields.add(new RecordEntryCodeGenerator(field.getIdentifier(), field.getType(), new RealExprCodeGenerator((float) realExpr.getValue())));
            } else if (expression instanceof BooleanExpr booleanExpr) {
                this.fields.add(new RecordEntryCodeGenerator(field.getIdentifier(), field.getType(), new BooleanExprCodeGenerator(booleanExpr.getValue())));
            } else if (expression instanceof StringExpr stringExpr) {
                this.fields.add(new RecordEntryCodeGenerator(field.getIdentifier(), field.getType(), new StringExprCodeGenerator(stringExpr.getValue())));
            } else if (expression instanceof ArrayExpr arrayExpr) {
                this.fields.add(new RecordEntryCodeGenerator(field.getIdentifier(), field.getType(), new ArrayExprCodeGenerator(arrayExpr.getType(), arrayExpr.getSize(), arrayExpr.getContent())));
            } else if (expression instanceof BinaryExpr binaryExpr) {
                this.fields.add(new RecordEntryCodeGenerator(field.getIdentifier(), field.getType(), new BinaryExprCodeGenerator(binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator())));
            } else if (expression instanceof IdentifierExpr identifierExpr) {
                this.fields.add(new RecordEntryCodeGenerator(field.getIdentifier(), field.getType(), new IdentifierExprCodeGenerator(identifierExpr.getIdentifier())));
            } else if (expression == null) {
                this.fields.add(new RecordEntryCodeGenerator(field.getIdentifier(), field.getType(), null));
            } else {
                throw new IllegalStateException("Unexpected value: " + expression + " for field " + field.getIdentifier());
            }
        }
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        ClassWriter recordWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        recordWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, this.name, null, "java/lang/Object", null);

        for (RecordEntryCodeGenerator entry : fields) {
            entry.generateCode(recordWriter, null);
        }

        recordWriter.visitEnd();
        byte[] recordClass = recordWriter.toByteArray();
    }
}
