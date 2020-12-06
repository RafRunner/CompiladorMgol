package dominio.enums;

public enum Tipo {

    INTEIRO("int"),
    LITERAL("literal"),
    REAL("float");

    private final String tipoEmC;

    Tipo(final String tipoEmC) {
        this.tipoEmC = tipoEmC;
    }

    @Override
    public String toString() {
        return tipoEmC;
    }
}
