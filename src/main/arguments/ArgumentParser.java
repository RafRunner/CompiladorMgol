package main.arguments;

import java.util.List;

public class ArgumentParser {

    final List<Argument> expectedArguments;

    public ArgumentParser(final List<Argument> expectedArguments, final String[] args) throws InvalidArgumentException {
        this.expectedArguments = expectedArguments;

        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];

            if (arg.length() == 2) {
                final Argument argument = expectedArguments.stream()
                        .filter(argument1 -> ("-" + argument1.flag).equals(arg)).findFirst().orElse(null);

                if (argument == null) {
                    throw new InvalidArgumentException("Argumentos inválidos!");
                }

                if (argument.type == ArgumentType.FLAG) {
                    argument.setValue(String.valueOf(!(boolean) argument.parse()));
                    continue;
                }

                i++;
                argument.setValue(args[i]);
            }
            else {
                final char[] flags = arg.substring(1).toCharArray();
                for (final char flag : flags) {
                    final Argument argument = expectedArguments.stream()
                            .filter(argument1 -> argument1.flag == flag && argument1.type == ArgumentType.FLAG).findFirst().orElse(null);

                    if (argument == null) {
                        throw new InvalidArgumentException("Argumentos inválidos!");
                    }
                    argument.setValue(String.valueOf(!(boolean) argument.parse()));
                }
            }
        }
    }

    private Argument findArgumentByName(final char flag) {
        return expectedArguments.stream().filter(argument -> argument.flag == flag).findFirst().orElse(null);
    }

    private Object getArgument(final char flag, String expectedType) throws InvalidArgumentException {
        final Argument argument = findArgumentByName(flag);
        try {
            return argument.parse();
        } catch (Exception ignored) {
            throw new InvalidArgumentException("Argumento " + argument.name + " \"-" + argument.flag + "\" deve ser " + expectedType);
        }
    }

    public String getStringArgument(final char flag) throws InvalidArgumentException {
        return (String) getArgument(flag, "string");
    }

    public int getIntArgument(final char flag) throws InvalidArgumentException {
        return (int) getArgument(flag, "inteiro");
    }

    public boolean getBollArgument(final char flag) throws InvalidArgumentException {
        return (boolean) getArgument(flag, "booleano");
    }
}
