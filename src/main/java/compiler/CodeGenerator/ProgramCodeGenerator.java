package compiler.CodeGenerator;
import compiler.Parser.GeneralDecl;
import compiler.Parser.ProcDecl;
import compiler.Parser.Program;
import compiler.Parser.RecordDecl;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

public class ProgramCodeGenerator {
    private final Program program;
    private final ArrayList<GeneralDeclCodeGenerator> declarations = new ArrayList<>();;

    public ProgramCodeGenerator(Program program) {
        this.program = program;
    }

    /**
     * Here, we use a polymorphic list and make good use of java's polymorphism and inheritance properties
     */
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        for (GeneralDeclCodeGenerator declaration : this.program.getContent()) {
            if (declaration instanceof RecordDecl recordDecl) {
                // Generate code for record declaration
                recordDecl.generateCode(writer, mv);
            } else if (declaration instanceof ProcDecl procDecl) {
                // Generate code for procedure declaration
                procDecl.generateCode(writer, mv);
            } else {
                // Generate code for other declarations
                declaration.generateCode(writer, mv);
            }
        }
    }
}
