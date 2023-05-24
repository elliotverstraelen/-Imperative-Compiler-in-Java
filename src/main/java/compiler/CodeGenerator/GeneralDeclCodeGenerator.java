package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class GeneralDeclCodeGenerator extends DeclarationCodeGenerator {
    private List<DeclarationCodeGenerator> declarations;

    public GeneralDeclCodeGenerator(List<DeclarationCodeGenerator> declarations) {
        this.declarations = declarations;
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        for (DeclarationCodeGenerator decl : declarations) {
            decl.generateCode(writer, mv);
        }
    }
}

