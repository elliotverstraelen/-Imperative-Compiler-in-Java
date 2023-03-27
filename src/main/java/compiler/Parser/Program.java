package compiler.Parser;

import java.util.ArrayList;
class Program {
    // Grammar : program -> procDecl* constDecl* recordDecl* globalDecl*
    private final ArrayList<ProcDecl> procDeclDecls;
    private final ArrayList<RecordT> recordDecls;
    private final ArrayList<GeneralDecl> globalDecls;
    public Program(ArrayList<ProcDecl> procDeclDecls, ArrayList<RecordT> recordDecl, ArrayList<GeneralDecl> globalDecls) {
        this.procDeclDecls = procDeclDecls;
        this.recordDecls = recordDecl;
        this.globalDecls = globalDecls;
    }

    public ArrayList<ProcDecl> getProcDeclDecls() {
        return procDeclDecls;
    }

    public ArrayList<RecordT> getRecordDecls() {
        return recordDecls;
    }

    public ArrayList<GeneralDecl> getGlobalDecls() {
        return globalDecls;
    }

    public void add(ProcDecl procDecl) {
        procDeclDecls.add(procDecl);
    }

    public void add(RecordT recordDecl) {
        recordDecls.add(recordDecl);
    }

    public void add(GeneralDecl globalDecl) {
        globalDecls.add(globalDecl);
    }
}
