package main.arguments;

public class StringArgument extends Argument {

    public StringArgument(final String name, final char flag, final String defaultValue) {
        super(name, flag, defaultValue);
    }

    public String parseStringValue() {
        return getValueToParse();
    }
}
