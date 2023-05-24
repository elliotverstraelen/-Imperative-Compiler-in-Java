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
            case "real":
                return "F";
            case "boolean":
                return "Z";
            case "string":
                return "S";
            case "record":
                return "R";
            case "proc":
                return "P";
            case "void":
                return "V";
            default:
                // Assume it's a fully qualified class name for user-defined types
                return "L" + typeName.replace('.', '/') + ";";
        }
    }
}
