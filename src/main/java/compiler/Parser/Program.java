package compiler.Parser;

import compiler.SemanticAnalyser.SemanticException;

import java.util.ArrayList;
import java.util.List;
public class Program implements ASTNode {
    private final List<GeneralDecl> declarations;
    public Program(List<GeneralDecl> declarations) {
        this.declarations = new ArrayList<>();
    }
    public List<GeneralDecl> getDeclarations() {
        return declarations;
    }

    public void addDeclaration(GeneralDecl declaration) {
        declarations.add(declaration);
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
        for (GeneralDecl declaration : declarations) {
            declaration.accept(visitor);
        }
    }
}
