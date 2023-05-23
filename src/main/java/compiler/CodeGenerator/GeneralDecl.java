package compiler.CodeGenerator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class GeneralDecl extends Declaration {
    private List<Declaration> declarations;

    public GeneralDecl(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        for (Declaration decl : declarations) {
            decl.generateCode(writer, mv);
        }
    }
}

