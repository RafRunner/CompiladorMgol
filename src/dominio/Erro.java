package dominio;

public class Erro {

    private final String mensgem;
    private final int linha;
    private final int coluna;

    public Erro(final String mensgem, final int linha, final int coluna) {
        this.mensgem = mensgem;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getMensgem() {
        return mensgem;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    @Override
    public String toString() {
        return "Erro na linha " + getLinha() + " coluna " + getColuna() + ": " + getMensgem();
    }
}
