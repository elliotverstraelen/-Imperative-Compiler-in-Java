package compiler.CodeGenerator;
import compiler.Parser.Program;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

public class ProgramCodeGenerator {
    private List<Declaration> declarations = new ArrayList<>();;

    /**
     * Here, we use a polymorphic list and make good use of java's polymorphism and inheritance properties
     */
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        for (Declaration declaration : declarations) {
            declaration.generateCode(writer, mv);
        }
    }

}
public class ProgramCodeGeneratorAdapter extends ProgramCodeGenerator {
    private Program program;

    public ProgramCodeGeneratorAdapter(Program program) {
        this.program = program;
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        // Here you might need to convert the data from `program` into a format that can be used to generate code
        // Then you call the `generateCode` method of each declaration in the `program` instance
        for (Declaration declaration : program.getDecl()) {
            declaration.generateCode(writer, mv);
        }
    }
}
