package partesCompilador.analisadorSintatico;

import dominio.Analisador;
import dominio.TokenLocalizado;
import dominio.enums.Token;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSintatico.tabela.*;

import java.util.*;

public class AnalisadorSintatico extends Analisador {

    private final Deque<Integer> pilhaEstados = new ArrayDeque<>();
    private final TabelaSintatica tabelaSintatica = new TabelaSintatica();
    private final AnalisadorLexico analisadorLexico;

    public AnalisadorSintatico(final AnalisadorLexico analisadorLexico, final int verbosidade) {
        super(analisadorLexico.getErros(), verbosidade);
        this.analisadorLexico = analisadorLexico;
    }

    public void analisa() {
        TokenLocalizado tokenAtual = analisadorLexico.lerProximoTokenNaoComentario();

        // Iniciando variáveis axiliares para o tratamento de erro
        int linhaAtual = tokenAtual.getLinha();
        final Deque<Integer> pilhaLinhaAnterior = new ArrayDeque<>();
        boolean lerProximoToken;
        TokenLocalizado tokenAnterior = tokenAtual;

        // A pilha começa com o estado 0
        pilhaEstados.push(0);
        pilhaLinhaAnterior.push(0);

        while (true) {
            final Integer estado = pilhaEstados.peek();
            Action action = tabelaSintatica.action(estado, tokenAtual.getToken());
            lerProximoToken = true;

            if (action instanceof ErroSintatico) {
                final ErroSintatico erro = (ErroSintatico) action;
                final int linhaErro;
                final int colunaErro;

                // Faltando ponto e vírgula. Substituíndo a action pela action que seria gerada pelo ; e não lendo o próximo
                if (erro.getTipoErro() == TipoErro.E1) {
                    lerProximoToken = false;
                    linhaErro = tokenAnterior.getLinha();
                    colunaErro = tokenAnterior.getColuna();
                    action = tabelaSintatica.action(estado, Token.pt_v);
                }

                // Entrando na recuperação de erro genérica (modo de pânico por linha)
                else {
                    linhaErro = tokenAtual.getLinha();
                    colunaErro = tokenAtual.getColuna();

                    // Vamos ignorar tokens até chegar na próxima linha (ou em eof) e então restaurar a pilha
                    TokenLocalizado tokenIgnorado = analisadorLexico.lerProximoTokenNaoComentario();
                    while (tokenIgnorado.getLinha() == linhaAtual && tokenIgnorado.getToken() != Token.eof) {
                        tokenIgnorado = analisadorLexico.lerProximoTokenNaoComentario();
                    }

                    // O token a ser analisado é o primeiro da nova linha
                    tokenAtual = tokenIgnorado;
                    // Atualizando a linha
                    linhaAtual = tokenAtual.getLinha();
                    // Restaurando a pilhaDeEstados para como estava na linha anterior (antes do erro)
                    copiaPilha(pilhaEstados, pilhaLinhaAnterior);
                }

                if (tokenAtual.getToken() != Token.erro) {
                    criaRegistraEImprimeErro("Erro sintático: " + erro.montaDetalhe(tokenAtual), linhaErro, colunaErro);
                }

                if (tokenAtual.getToken() == Token.eof) {
                    imprimeAviso("Não foi possível terminar a derivação gramatical do programa devido a erros. Análise será limitada");
                    break;
                }
            }

            if (action instanceof Shift) {
                final Integer novoEstado = ((Shift) action).getEstado();
                pilhaEstados.push(novoEstado);

                if (lerProximoToken) {
                    tokenAnterior = tokenAtual;
                    tokenAtual = analisadorLexico.lerProximoTokenNaoComentario();
                }

                // Salvando o pilha da linha anterior antes de ir para a nova linha que pode conter erros (a linha será ignorada se tiver)
                if (tokenAtual.getLinha() != linhaAtual) {
                    linhaAtual = tokenAtual.getLinha();
                    copiaPilha(pilhaLinhaAnterior, pilhaEstados);
                }
            }

            else if (action instanceof Reduce) {
                final RegraGramatical regra = ((Reduce) action).getRegraGramatical();

                for (int i = 0; i < regra.getLadoDireito().size(); i++) {
                    pilhaEstados.pop();
                }

                final Integer Goto = tabelaSintatica.Goto(pilhaEstados.peek(), regra.getLadoEsquedo());
                pilhaEstados.push(Goto);

                imprimeInfo(regra);
            }

            else if (action instanceof Accept) {
                imprimeInfo(RegraGramatical.r01);
                break;
            }
        }
    }

    // Torna a pilha dest uma cópia da pilha src
    private static void copiaPilha(final Deque<Integer> dest, final Deque<Integer> src) {
        dest.clear();
        for (final Integer i : src) {
            dest.addLast(i);
        }
    }
}
