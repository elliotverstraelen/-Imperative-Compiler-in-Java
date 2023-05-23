package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;

public class Program {
    // Grammar : program -> procDecl* constDecl* recordDecl* globalDecl*
    private final ArrayList<ProcDecl> procDecls;
    private final ArrayList<RecordDecl> recordDecls;
    private final ArrayList<GeneralDecl> globalDecls;
    public Program(ArrayList<ProcDecl> procDeclDecls, ArrayList<RecordDecl> recordDecl, ArrayList<GeneralDecl> globalDecls) {
        this.procDecls = procDeclDecls;
        this.recordDecls = recordDecl;
        this.globalDecls = globalDecls;
    }
    public ArrayList<Object> getContent() {
        ArrayList<Object> content = new ArrayList<>();
        content.addAll(procDecls);
        content.addAll(recordDecls);
        content.addAll(globalDecls);
        return content;
    }

    public ArrayList<GeneralDecl> getGlobalDecls() { return globalDecls; }

    public void add(ProcDecl procDecl) {
        procDecls.add(procDecl);
    }

    public void add(RecordDecl recordDecl) { recordDecls.add(recordDecl); }

    public void add(GeneralDecl globalDecl) {
        globalDecls.add(globalDecl);
    }

    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Program{" +
                "procDecls=" + procDecls +
                ", recordDecls=" + recordDecls +
                ", globalDecls=" + globalDecls +
                '}';
    }
}
