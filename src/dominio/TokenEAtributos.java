package dominio;

import dominio.enums.Tipo;
import dominio.enums.Token;

public class TokenEAtributos {

    private final Token token;
    private final String lexema;
    private final Tipo tipo;

    public TokenEAtributos(Token token, String lexema, Tipo tipo) {
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
}
