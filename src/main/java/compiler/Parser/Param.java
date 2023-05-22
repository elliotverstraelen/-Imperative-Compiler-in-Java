package compiler.Parser;

public class Param {
    public final String name;
    public final Type type;

    public Param(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String toString() {
        return "Param{" +
                "name=" + name +
                ", type=" + type.name +
                '}';
    }
}
