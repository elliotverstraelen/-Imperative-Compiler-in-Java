package compiler.CodeGenerator;

import compiler.Parser.RecordEntry;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;

public class RecordExprCodeGenerator extends ExpressionCodeGenerator {
    protected final ArrayList<RecordEntryCodeGenerator> content; // Content of the record

    public RecordExprCodeGenerator(String type, ArrayList<RecordEntry> content) {
        super("RealExpr");
        this.content = new ArrayList<>();
        for (RecordEntry e : content) {
            this.content.add(new RecordEntryCodeGenerator(e.getIdentifier(), e.getType(), castExpr(e.getValue())));
        }
    }

    public ArrayList<RecordEntryCodeGenerator> getContent() {
        return content;
    }

    @Override
    public void generateCode(MethodVisitor visitor) {
        for (RecordEntryCodeGenerator e : content) {
            visitor.visitInsn(Opcodes.DUP);
        }
    }
}
