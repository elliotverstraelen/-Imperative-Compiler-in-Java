package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GeneralDeclCodeGenerator extends DeclarationCodeGenerator {
    protected final String name;
    protected final String typeDescriptor;
    protected final ExpressionCodeGenerator initExpr;
    protected String className;

    public GeneralDeclCodeGenerator(String name, ExpressionCodeGenerator initExpr, String type) {
       this.name = name;
        this.initExpr = initExpr;
        this.typeDescriptor = getTypeDescriptor(type);
    }

    /**
     * This is used to handle the difference between ints and floats
     */
    private String getTypeDescriptor(String type) {
        return switch (type) {
            case "int" -> "I";
            case "real", "float" -> "F";
            case "bool" -> "B";
            case "string" -> "Ljava/lang/String;";
            case "proc" -> "Ljava/lang/Runnable;";
            case "R" -> "Lcompiler/CodeGenerator/Record;";
            default -> {
                if (type.contains("[]")) {
                    // array
                    yield "[L" + type.replace("[]", "") + ";";
                } else if (Character.isUpperCase(type.charAt(0))) {
                    // record
                    yield "Lcompiler/CodeGenerator/Record;";
                } else {
                    throw new IllegalArgumentException("Unsupported type: " + type);
                }
            }
        };
    }

    public void generateCode(ClassWriter writer, MethodVisitor mv, String className) {
        this.className = className;
        generateCode(writer, mv);
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        if(className == null) {
            className = name;
        }
        writer.visitField(Opcodes.ACC_PUBLIC, name, typeDescriptor, null, null);
        if (initExpr != null) {
            initExpr.generateCode(mv);
            mv.visitFieldInsn(Opcodes.PUTSTATIC, className.replace('.', '/'), name, typeDescriptor);
        }
    }
}

