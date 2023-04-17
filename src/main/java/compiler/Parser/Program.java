package compiler.Parser;

import compiler.Exceptions.SemanticException;

import java.util.ArrayList;
import java.util.List;
public class Program implements ASTNode {
    private final List<Object> content; // List of declarations or statements
    public Program() {
        this.content = new ArrayList<>();
    }
    public List<Object> getContent() {
        return content;
    }

    public void add(Object declaration) {
        content.add(declaration);
    }

    @Override
    public void accept(ASTVisitor visitor) throws SemanticException {
        visitor.visit(this);
    }
}
