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
        for (GeneralDecl declaration : this.program.getContent()) {
            if (declaration instanceof RecordDecl recordDecl) {
                // Generate code for record declaration
                declarations.add(new RecordDeclCodeGenerator(recordDecl.getName(), recordDecl.getFields()));
            } else if (declaration instanceof ProcDecl procDecl) {
                // Generate code for procedure declaration
                declarations.add(new ProcDeclCodeGenerator(procDecl.getIdentifier(), procDecl.getParams(), procDecl.getBody()));
            } else {
                // Generate code for other declarations
                declarations.add(new GeneralDeclCodeGenerator(declaration.getIdentifier(), ExpressionCodeGenerator.castExpr(declaration.getValue()), declaration.getType().getName()));
            }
        }
        for (GeneralDeclCodeGenerator declaration : declarations) {
            declaration.generateCode(writer, mv);
        }
    }
}
