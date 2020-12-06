package dominio;

import dominio.enums.Tipo;
import dominio.enums.Token;

import java.util.Objects;

public class TokenEAtributos {

    private final Token token;
    private String lexema;
    private Tipo tipo;

    public TokenEAtributos(final Token token, final String lexema, final Tipo tipo) {
        this.token = token;
        this.lexema = lexema;
        this.tipo = tipo;
    }

    public TokenEAtributos() {
        this(null, null, null);
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

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public TokenLocalizado localizar(final int linha, final int coluna) {
        return new TokenLocalizado(this, linha, coluna);
    }

    // Versão usada para quando criamos tokens não existentes para coreção de erros. Posições são nagativas porque o token não existe de verdade
    public TokenLocalizado localizar() {
        return new TokenLocalizado(this, -2, -1);
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
