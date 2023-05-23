package compiler.CodeGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.List;

public class ProcCallCodeGenerator {
    private String identifier;
    private List<Expression> args;

    public ProcCallCodeGenerator(String identifier, List<Expression> args) {
        this.identifier = identifier;
        this.args = args;
    }

    public void generateCode(MethodVisitor visitor) {
        for (Expression arg : args) {
            arg.generateCode(visitor);
        }
        visitor.visitMethodInsn(Opcodes.INVOKESTATIC, "MyClass", identifier,
                MethodType.methodType(void.class, Collections.nCopies(args.size(), int.class)).toMethodDescriptorString(), false);

    }
}
