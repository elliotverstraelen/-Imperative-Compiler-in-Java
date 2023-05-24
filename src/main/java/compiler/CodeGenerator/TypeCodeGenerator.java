package compiler.CodeGenerator;

public class TypeCodeGenerator {
    private final String typeName;

    public TypeCodeGenerator(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String toDescriptor() {
        switch (typeName) {
            case "int":
                return "I";
            case "boolean":
                return "Z";
            case "char":
                return "C";
            case "byte":
                return "B";
            case "short":
                return "S";
            case "long":
                return "J";
            case "float":
                return "F";
            case "double":
                return "D";
            case "void":
                return "V";
            default:
                // Assume it's a fully qualified class name for user-defined types
                return "L" + typeName.replace('.', '/') + ";";
        }
    }
}
