package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;

public class ParamCodeGenerator {
    private String name;

    public ParamCodeGenerator(String name) {
        this.name = name;
    }

    public void generateCode(MethodVisitor mv) {
        // In the context of JVM, parameters are loaded onto the stack in the method body.
        // Code generation for parameters might not be applicable or it should be done inside the method body.
    }
}
