package main.arguments;

public class IntegerArgument extends Argument {

    public IntegerArgument(final String name, final char flag, final String defaultValue) {
        super(name, flag, defaultValue);
    }

    public int parseIntValue() {
        return Integer.parseInt(getValueToParse());
    }
}
