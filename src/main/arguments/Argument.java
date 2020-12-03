package main.arguments;

public abstract class Argument {

    private final String name;
    private final char flag;
    protected final String defaultValue;

    private String value;

    public Argument(final String name, final char flag, final String defaultValue) {
        this.name = name;
        this.flag = flag;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public char getFlag() {
        return flag;
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
}
