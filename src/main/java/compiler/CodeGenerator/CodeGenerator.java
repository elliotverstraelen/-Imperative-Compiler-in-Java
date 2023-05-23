package compiler.CodeGenerator;

import compiler.Parser.Program;
import org.objectweb.asm.ClassWriter;

public class CodeGenerator {
    private ClassWriter writer;

    public static byte[] generateByteCode(Program program) {
        // logic to generate code from a Program object
        return new byte[0];
    }

    public static String generateCode(Program program) {
        // logic to generate code from a Program object
        return "";
    }
}



// Procedure Call CodeGen
/*
public class ProcCall {
    private String identifier;
    private List<Expression> args;

    public ProcCall(String identifier, List<Expression> args) {
        this.identifier = identifier;
        this.args = args;
    }

    public void generateCode(MethodVisitor visitor) {
        for (Expression arg : args) {
            arg.generateCode(visitor);
        }
        visitor.visitMethodInsn(Opcodes.INVOKESTATIC, identifier,
                MethodType.methodType(void.class, Collections.nCopies(args.size(), int.class)).toMethodDescriptorString(), true);

    }
}*/
