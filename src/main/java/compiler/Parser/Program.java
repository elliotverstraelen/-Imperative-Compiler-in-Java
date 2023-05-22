package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;
public class Program {
    // Grammar : program -> procDecl* constDecl* recordDecl* globalDecl*
    private final ArrayList<ProcDecl> procDeclDecls;
    private final ArrayList<RecordT> recordDecls;
    private final ArrayList<GeneralDecl> globalDecls;
    public Program(ArrayList<ProcDecl> procDeclDecls, ArrayList<RecordT> recordDecl, ArrayList<GeneralDecl> globalDecls) {
        this.procDeclDecls = procDeclDecls;
        this.recordDecls = recordDecl;
        this.globalDecls = globalDecls;
    }
    public List<Object> getContent() {
        return content;
    }

    public boolean hasProcDeclDecls() { return !procDeclDecls.isEmpty(); }

    public ArrayList<RecordT> getRecordDecls() {
        return recordDecls;
    }
    
    public boolean hasRecordDecls() { return !recordDecls.isEmpty(); }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
    
    public boolean hasGlobalDecls() { return !globalDecls.isEmpty(); }

    public void add(ProcDecl procDecl) {
        procDeclDecls.add(procDecl);
    }

    public void add(RecordT recordDecl) {
        recordDecls.add(recordDecl);
    }

    public void add(GeneralDecl globalDecl) {
        globalDecls.add(globalDecl);
    }

    @Override
    public String toString() {
        return "Program{" +
                "procDeclDecls=" + procDeclDecls +
                ", recordDecls=" + recordDecls +
                ", globalDecls=" + globalDecls +
                '}';
    }
}
