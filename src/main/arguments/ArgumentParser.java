package main.arguments;

import java.util.List;

public class ArgumentParser {

    final List<Argument> expectedArguments;

    public ArgumentParser(final List<Argument> expectedArguments, final String[] args) {
        this.expectedArguments = expectedArguments;

        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];

            final Argument argument = expectedArguments.stream().filter(argument1 -> ("-" + argument1.getFlag()).equals(arg)).findFirst().orElse(null);
            if (argument == null) {
                throw new RuntimeException("Argumentos inválidos!");
            }

            if (argument instanceof FlagArgument) {
                final FlagArgument flagArgument = (FlagArgument) argument;
                flagArgument.setValue(String.valueOf(!flagArgument.parseBoolValue()));
                continue;
            }

            i++;
            argument.setValue(args[i]);
        }
    }

    private Argument findArgumentByName(final char flag) {
        return expectedArguments.stream().filter(argument -> argument.getFlag() == flag).findFirst().orElse(null);
    }

    private String buildInvalidTypeError(final Argument argument) {
        return "Argumento " + argument.getName() + " \"-" + argument.getFlag() + "\" deve ser uma ";
    }

    public String getStringArgument(final char flag) {
        final StringArgument argument = ((StringArgument) findArgumentByName(flag));
        try {
            return argument.parseStringValue();
        } catch (Exception ignored) {
            throw new RuntimeException(buildInvalidTypeError(argument) + "string");
        }
    }

    public int getIntArgument(final char flag) {
        final IntegerArgument argument = ((IntegerArgument) findArgumentByName(flag));
        try {
            return argument.parseIntValue();
        } catch (Exception ignored) {
            throw new RuntimeException(buildInvalidTypeError(argument) + "inteiro");
        }
    }

    public boolean getBollArgument(final char flag) {
        final BooleanArgument argument = ((BooleanArgument) findArgumentByName(flag));
        try {
            return argument.parseBoolValue();
        } catch (Exception ignored) {
            throw new RuntimeException(buildInvalidTypeError(argument) + "booleano");
        }
    }
}
