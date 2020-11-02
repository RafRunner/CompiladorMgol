package dominio;

import dominio.enums.Tipo;
import dominio.enums.Token;

public class TokenLocalizado {

    private final TokenEAtributos tokenEAtributos;
    private final int linha;
    private final int coluna;

    public TokenLocalizado(final TokenEAtributos tokenEAtributos, final int linha, final int coluna) {
        this.tokenEAtributos = tokenEAtributos;
        this.linha = linha;
        this.coluna = coluna;
    }

    public TokenEAtributos getTokenEAtributos() {
        return tokenEAtributos;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public Token getToken() {
        return tokenEAtributos.getToken();
    }

    public String getLexema() {
        return tokenEAtributos.getLexema();
    }

    public Tipo getTipo() {
        return tokenEAtributos.getTipo();
    }

    public void setTipo(final Tipo tipo) {
        tokenEAtributos.setTipo(tipo);
    }

    @Override
    public String toString() {
        return tokenEAtributos.toString() + ", linha: " + (linha + 1) + ", coluna: " + coluna;
    }
}
