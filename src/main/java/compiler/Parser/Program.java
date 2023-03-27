package compiler.Parser;

import java.util.ArrayList;
class Program {
    // Grammar : program -> procDecl* constDecl* recordDecl* globalDecl*
    private final ArrayList<ProcDecl> procDeclDecls;
    private final ArrayList<Record> recordDecl;
    private final ArrayList<GeneralDecl> globalDecls;
    public Program(ArrayList<ProcDecl> procDeclDecls, ArrayList<Record> recordDecl, ArrayList<GeneralDecl> globalDecls) {
        this.procDeclDecls = procDeclDecls;
        this.recordDecl = recordDecl;
        this.globalDecls = globalDecls;
    }
}
