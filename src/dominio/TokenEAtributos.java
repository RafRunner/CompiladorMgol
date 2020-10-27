package dominio;

import dominio.enums.Tipo;
import dominio.enums.Token;

import java.util.Objects;

public class TokenEAtributos {

    private final Token token;
    private final String lexema;
    private Tipo tipo;

    public TokenEAtributos(final Token token, final String lexema, final Tipo tipo) {
        this.token = token;
        this.lexema = lexema;
        this.tipo = tipo;
    }

    public Token getToken() {
        return token;
    }

    public String getLexema() {
        return lexema;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(final Tipo tipo) {
        this.tipo = tipo;
    }

    public TokenLocalizado localizar(final int linha, final int coluna) {
        return new TokenLocalizado(this, linha, coluna);
    }

    @Override
    public String toString() {
        return "TokenEAtributos [ token: " + token + ", lexema: '" + lexema + "', tipo: " + tipo + " ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TokenEAtributos)){
            return false;
        }
        final TokenEAtributos outro = (TokenEAtributos) o;
        return getToken() == outro.getToken() && getLexema().equals(outro.getLexema()) && getTipo() == outro.getTipo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken(), getLexema(), getTipo());
    }
}
