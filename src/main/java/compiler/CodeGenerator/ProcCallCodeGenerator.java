package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.List;

public class ProcCallCodeGenerator {
    private String identifier;
    private List<ExpressionCodeGenerator> args;

    public ProcCallCodeGenerator(String identifier, List<ExpressionCodeGenerator> args) {
        this.identifier = identifier;
        this.args = args;
    }

    public void generateCode(MethodVisitor visitor) {
        for (ExpressionCodeGenerator arg : args) {
            arg.generateCode(visitor);
        }
        visitor.visitMethodInsn(Opcodes.INVOKESTATIC, "MyClass", identifier,
                MethodType.methodType(void.class, Collections.nCopies(args.size(), int.class)).toMethodDescriptorString(), false);

    }
}
