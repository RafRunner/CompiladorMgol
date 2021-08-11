package main.arguments;

public class Argument {

    final ArgumentType type;
    final String name;
    final char flag;
    final String defaultValue;

    private String value;

    // não faz sentido defaultValue em argumentos do tipo flag
    public Argument(final ArgumentType type, final String name, final char flag) {
        this(type, name, flag, "");
    }

    public Argument(final ArgumentType type, final String name, final char flag, final String defaultValue) {
        this.type = type;
        this.name = name;
        this.flag = flag;
        this.defaultValue = defaultValue;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNameWithoutSpaces() {
        return name.replaceAll(" ", "_");
    }

    protected String getValueToParse() {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Object parse() {
        return type.parse(getValueToParse());
    }
}
