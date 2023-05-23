package compiler.CodeGenerator;

import compiler.Lexer.Lexer;
import compiler.Parser.Param;
import compiler.Parser.Program;
import compiler.Parser.RecordEntry;
import compiler.Parser.Stmt;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.List;

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




