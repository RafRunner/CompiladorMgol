package main.arguments;

public class FlagArgument extends BooleanArgument {

    public FlagArgument(String name, char flag, String defaultValue) {
        super(name, flag, defaultValue);
    }
}
