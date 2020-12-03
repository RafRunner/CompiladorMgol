package main.arguments;

public class BooleanArgument extends Argument {

    public BooleanArgument(final String name, final char flag, final String defaultValue) {
        super(name, flag, defaultValue);
    }

    public boolean parseBoolValue() {
        return Boolean.parseBoolean(getValueToParse());
    }
}
