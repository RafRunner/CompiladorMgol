package dominio;

import dominio.enums.Cor;

import java.util.Set;

public abstract class Analisador {

    private final Set<Erro> erros;

    // Verbosidade 0 -> não imprime nada
    // Verbosidade 1 -> imprime erros e avisos
    // Verbosidade 2 -> imprime tudo (informação do processamento/análise)
    private final int verbosidade;

    public Analisador(Set<Erro> erros, int verbosidade) {
        this.erros = erros;
        this.verbosidade = verbosidade;
    }

    private void imprimeSeVerbosidadeSuficiente(final Object mensagem, final int vebosidadeMinima, final Cor cor) {
        if (verbosidade >= vebosidadeMinima) {
            Cor.imprimeComCor(mensagem, cor);
        }
    }

    protected void imprimeErro(final Object mensagem) {
        imprimeSeVerbosidadeSuficiente(mensagem, 1, Cor.RED);
    }

    protected void criaRegistraEImprimeErro(final Object mensagem, final int linha, final int coluna) {
        final Erro erro = new Erro(mensagem.toString(), linha + 1, coluna);
        erros.add(erro);
        imprimeErro(erro);
    }

    protected void imprimeAviso(final Object mensagem) {
        imprimeSeVerbosidadeSuficiente(mensagem, 1, Cor.YELLOW);
    }

    protected void imprimeInfo(final Object mensagem) {
        imprimeSeVerbosidadeSuficiente(mensagem, 2, Cor.WHITE);
    }

    public Set<Erro> getErros() {
        return erros;
    }

    public int getVerbosidade() {
        return verbosidade;
    }
}
