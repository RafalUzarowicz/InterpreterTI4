package Parser.ProgramTree.Value;

public class Literal extends Value {
    public enum LiteralType{
        Int,
        String,
        Bool,
        Unit,
        Color,
        Hex,
        Planet
    }
    private LiteralType type;
    private String value;
    public Literal(LiteralType type, String value){
        this.type = type;
        this.value = value;
    }

    public LiteralType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
