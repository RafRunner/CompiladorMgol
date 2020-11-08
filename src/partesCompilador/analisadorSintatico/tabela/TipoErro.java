package partesCompilador.analisadorSintatico.tabela;

import dominio.TokenEAtributos;
import dominio.TokenLocalizado;

public enum TipoErro {

    // Espaços vazios são E0
    E0(token -> "Token do tipo \"" + token.getToken() + "\" inesperado: " + token.getLexema()),
    E1(token -> "Faltando ponto e vírgula");

    private interface Detalhe {
        String montaDetalhe(final TokenLocalizado token);
    }

    private final Detalhe detalhe;

    TipoErro(final Detalhe detalhe) {
        this.detalhe = detalhe;
    }

    public String getDetalhe(final TokenLocalizado token) {
        return detalhe.montaDetalhe(token);
    }
}
