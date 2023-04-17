package compiler.Parser;

public class Type {
    private final String name;

    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() { return name;  }
}