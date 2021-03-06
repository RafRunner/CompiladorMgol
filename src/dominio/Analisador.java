package dominio;

import dominio.enums.Cor;

import java.util.List;

public abstract class Analisador {

    private final List<Erro> erros;

    // Verbosidade 0 -> não imprime nada
    // Verbosidade 1 -> imprime erros e avisos
    // Verbosidade 2 -> imprime tudo (informação do processamento/análise)
    private final int verbosidade;
    private final Cor corInfo;

    public Analisador(final List<Erro> erros, final int verbosidade, final Cor corInfo) {
        this.erros = erros;
        this.verbosidade = verbosidade;
        this.corInfo = corInfo;
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
        final Erro erro = new Erro(mensagem.toString(), linha, coluna);

        // Não registramos erros duplicados ou erros em posições negativas pois foram gerados pelo próprio tratamento de erro
        if (!erros.contains(erro) && erro.getLinha() >= 0 && erro.getColuna() >= 0) {
            erros.add(erro);
        }

        imprimeErro(erro);
    }

    protected void imprimeAviso(final Object mensagem) {
        imprimeSeVerbosidadeSuficiente(mensagem, 1, Cor.YELLOW);
    }

    protected void imprimeInfo(final Object mensagem) {
        imprimeSeVerbosidadeSuficiente(mensagem, 2, corInfo);
    }

    public List<Erro> getErros() {
        return erros;
    }

    public int getVerbosidade() {
        return verbosidade;
    }
}
