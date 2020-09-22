package dominio;

import dominio.enums.Token;

public class Erro {

    private String mensgem;
    private int linha;
    private int coluna;

    public Erro(String mensgem, int linha, int coluna) {
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
        return "Erro na linha " + getLinha() + " coluna " + getLinha() + ": " + getMensgem();
    }
}
