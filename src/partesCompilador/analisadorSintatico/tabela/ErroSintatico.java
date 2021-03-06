package partesCompilador.analisadorSintatico.tabela;

import dominio.TokenLocalizado;

public class ErroSintatico extends Action {

    private final TipoErro tipoErro;

    public ErroSintatico(final TipoErro tipoErro) {
        this.tipoErro = tipoErro;
    }

    public String montaDetalhe(final TokenLocalizado token) {
        return tipoErro.getDetalhe(token);
    }

    public boolean usaAtual() {
        return tipoErro.usaAtual();
    }

    public TipoErro getTipo() {
        return tipoErro;
    }
}
