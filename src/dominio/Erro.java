package dominio;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Erro)) {
            return false;
        }
        final Erro erro = (Erro) o;
        return linha == erro.linha && coluna == erro.coluna && Objects.equals(mensgem, erro.mensgem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mensgem, linha, coluna);
    }

    @Override
    public String toString() {
        return "Erro na linha " + getLinha() + " coluna " + getColuna() + ": " + getMensgem();
    }
}
