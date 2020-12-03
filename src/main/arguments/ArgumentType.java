package main.arguments;

public enum ArgumentType {

    INTEGER(Integer::parseInt),
    BOOLEAN(Boolean::parseBoolean),
    FLAG(Boolean::parseBoolean),
    STRING(value -> value);

    private interface Parser {
        Object parse(String value);
    }

    private final Parser parser;

    ArgumentType(Parser parser) {
        this.parser = parser;
    }

    public Object parse(final String value) {
        return parser.parse(value);
    }
}
