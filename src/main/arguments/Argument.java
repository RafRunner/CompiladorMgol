package main.arguments;

public class Argument {

    final ArgumentType type;
    final String name;
    final char flag;
    final String defaultValue;

    private String value;

    public Argument(final ArgumentType type, final String name, final char flag, final String defaultValue) {
        this.type = type;
        this.name = name;
        this.flag = flag;
        this.defaultValue = defaultValue;
    }

    public void setValue(String value) {
        this.value = value;
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
