package compiler.CodeGenerator;

import compiler.Parser.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Collections;

import static compiler.CodeGenerator.ExpressionCodeGenerator.castExpr;

public class ProcDeclCodeGenerator extends GeneralDeclCodeGenerator {
    private String name;
    private ArrayList<Param> params;
    private ArrayList<StmtCodeGenerator> body;

    public ProcDeclCodeGenerator(String name, ArrayList<Param> params, ArrayList<StmtCodeGenerator> body) {
        super(name, null, "proc");
        this.params = params;
        this.body = body;
    }

    public ProcDeclCodeGenerator(String name, ArrayList<Param> params, Block body) {
        super(name, null, "proc");
        this.name = name;
        this.params = params;
        this.body = new ArrayList<>();
        for (Object stmt : body.getStatements()) {
            if (stmt instanceof ArrayAccess arrayAccess) {
                this.body.add(new ArrayStmtCodeGenerator(arrayAccess.getIdentifier(), castExpr(arrayAccess.getIndex())));
            } else if (stmt instanceof AssignmentStmt assignmentStmt) {
                this.body.add(new AssignmentCodeGenerator(new IdentifierExprCodeGenerator(assignmentStmt.getIdentifier()), castExpr(assignmentStmt.getValue())));
            } else if (stmt instanceof For forStmt) {
                //TODO
            } else if (stmt instanceof CtrlStruct ctrlStruct) {
                //TODO
            }else if (stmt instanceof ProcCall procCall) {
                //TODO
            }else if (stmt instanceof ReturnStmt returnStmt) {
                //TODO
            }
        }
    }

    public void generateCode(ClassWriter writer) {
        String descriptor = MethodType.methodType(void.class, Collections.nCopies(params.size(), int.class)).toMethodDescriptorString();
        MethodVisitor mv = writer.visitMethod(Opcodes.ACC_STATIC, name, descriptor, null, null);
        for (StmtCodeGenerator stmt : body) {
            stmt.generateCode(mv);
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(-1, -1); // Compute maximum stack and local variable size automatically
        mv.visitEnd();
    }

    @Override
    public void generateCode(ClassWriter writer, MethodVisitor mv) {
        // You may need to provide your own implementation here.
        // Following is just a stub implementation

        for (StmtCodeGenerator stmt : body) {
            stmt.generateCode(mv);
        }
        mv.visitInsn(Opcodes.RETURN);
    }
}
