package dominio.enums;

public enum Cor {

    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m");

    private final String codigo;

    Cor(final String codigo) {
        this.codigo = codigo;
    }

    public static void imprimeComCor(final Object mensagem, final Cor cor) {
        System.out.println(cor.toString() + mensagem.toString() + RESET.toString());
    }

    @Override
    public String toString() {
        return codigo;
    }
}
