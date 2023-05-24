package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class VariableDeclCodeGenerator extends DeclarationCodeGenerator {
    private String name;
    private ExpressionCodeGenerator initExpr;
    private String typeDescriptor;
    private String className;

    public VariableDeclCodeGenerator(String name, ExpressionCodeGenerator initExpr, String type) {
        this.name = name;
        this.initExpr = initExpr;
        this.typeDescriptor = getTypeDescriptor(type);
    }

    /**
     * This is used to handle the difference between ints and floats
     */
    private String getTypeDescriptor(String type) {
        switch (type) {
            case "int":
                return "I";
            case "float":
                return "F";
            // add more cases for other types as needed
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv, String className) {
        this.className = className;
        generateCode(writer, mv);
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        if(className == null) {
            throw new IllegalStateException("Class name not set.");
        }
        writer.visitField(Opcodes.ACC_PUBLIC, name, typeDescriptor, null, null);
        if (initExpr != null) {
            initExpr.generateCode(mv);
            mv.visitFieldInsn(Opcodes.PUTSTATIC, className.replace('.', '/'), name, typeDescriptor);
        }
    }
}
