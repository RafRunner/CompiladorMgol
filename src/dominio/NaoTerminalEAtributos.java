package dominio;

import dominio.enums.Tipo;
import dominio.enums.Token;
import partesCompilador.analisadorSintatico.NaoTerminal;


public class NaoTerminalEAtributos {

    private final NaoTerminal naoTerminal;
    private TokenEAtributos atributos;

    public NaoTerminalEAtributos(final NaoTerminal naoTerminal, final TokenEAtributos atributos) {
        this.naoTerminal = naoTerminal;
        this.atributos = atributos;
    }

    public NaoTerminal getNaoTerminal() {
        return naoTerminal;
    }

    public TokenEAtributos getAtributos() {
        return atributos;
    }

    public Token getToken() {
        return atributos.getToken();
    }

    public String getLexema() {
        return atributos.getLexema();
    }

    public Tipo getTipo() {
        return atributos.getTipo();
    }

    public void setAtributos(final TokenEAtributos atributos) {
        this.atributos = atributos;
    }

    @Override
    public String toString() {
        return naoTerminal + ": " + atributos;
    }
}
