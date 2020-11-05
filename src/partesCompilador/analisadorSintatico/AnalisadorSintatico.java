package partesCompilador.analisadorSintatico;

import dominio.Erro;
import dominio.TokenLocalizado;
import dominio.enums.Cor;
import dominio.enums.Token;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSintatico.tabela.*;

import java.util.*;

public class AnalisadorSintatico {

    private final Deque<Integer> pilhaEstados = new ArrayDeque<>();
    private final TabelaSintatica tabelaSintatica = new TabelaSintatica();
    private final AnalisadorLexico analisadorLexico;

    private final List<Erro> erros;

    // Verbosidade 0 -> não imprime nada
    // Verbosidade 1 -> imprime erros e avisos
    // Verbosidade 2 -> imprime tudo (erros e regras de derivação)
    private final int verbosidade;

    public AnalisadorSintatico(final AnalisadorLexico analisadorLexico, final int verbosidade) {
        this.analisadorLexico = analisadorLexico;
        this.erros = analisadorLexico.getErros();
        this.verbosidade = verbosidade;
    }

    public void analisa() {
        TokenLocalizado tokenAtual = analisadorLexico.lerProximoTokenNaoComentario();

        // Iniciando variáveis axiliares para o tratamento de erro
        int linhaAtual = tokenAtual.getLinha();
        final Deque<Integer> pilhaLinhaAnterior = new ArrayDeque<>();

        // A pilha começa com o estado 0
        pilhaEstados.push(0);
        pilhaLinhaAnterior.push(0);

        while (true) {
            final Integer estado = pilhaEstados.peek();
            final Action action = tabelaSintatica.action(estado, tokenAtual.getToken());

            if (action instanceof Shift) {
                final Integer novoEstado = ((Shift) action).getEstado();
                pilhaEstados.push(novoEstado);

                tokenAtual = analisadorLexico.lerProximoTokenNaoComentario();

                // Salvando o pilha da linha anterior antes de ir para a nova linha que pode conter erros (a linha será ignorada se tiver)
                if (tokenAtual.getLinha() != linhaAtual) {
                    linhaAtual = tokenAtual.getLinha();
                    pilhaLinhaAnterior.clear();
                    for (final Integer i : pilhaEstados) {
                        pilhaLinhaAnterior.addLast(i);
                    }
                }
            }

            else if (action instanceof Reduce) {
                final RegraGramatical regra = ((Reduce) action).getRegraGramatical();

                for (int i = 0; i < regra.getLadoDireito().size(); i++) {
                    pilhaEstados.pop();
                }

                final Integer Goto = tabelaSintatica.Goto(pilhaEstados.peek(), regra.getLadoEsquedo());
                pilhaEstados.push(Goto);

                if (verbosidade > 1) {
                    System.out.println(regra);
                }
            }

            else if (action instanceof Accept) {
                if (verbosidade > 1) {
                    System.out.println(RegraGramatical.r01);
                }
                break;
            }

            // Erro! Entrando na recuperação de erro
            else {
                if (tokenAtual.getToken() != Token.erro) {
                    final Erro erro = new Erro(
                            String.format("Erro sintático: Token do tipo \"%s\" inesperado: %s", tokenAtual.getToken(), tokenAtual.getLexema()),
                            tokenAtual.getLinha() + 1, tokenAtual.getColuna());
                    erros.add(erro);

                    if (verbosidade > 0) {
                        Cor.imprimeComCor("\n" + erro + "\n", Cor.RED);
                    }
                }

                if (tokenAtual.getToken() == Token.eof) {
                    if (verbosidade > 0) {
                        Cor.imprimeComCor("Não foi possível terminar a derivação gramatical do programa devido a erros. Análise será limitada\n", Cor.YELLOW);
                    }
                    break;
                }

                // Vamos ignorar tokens até chegar na próxima linha e então restaurar a pilha
                TokenLocalizado tokenIgnorado = analisadorLexico.lerProximoTokenNaoComentario();
                while (tokenIgnorado.getLinha() == linhaAtual && tokenIgnorado.getToken() != Token.eof) {
                    tokenIgnorado = analisadorLexico.lerProximoTokenNaoComentario();
                }

                // O token a ser analisado é o primeiro da nova linha
                tokenAtual = tokenIgnorado;
                // Atualizando a linha
                linhaAtual = tokenAtual.getLinha();
                // Restaurando a pilhaDeEstados para como estava na linha anterior (antes do erro)
                pilhaEstados.clear();
                for (final Integer i : pilhaLinhaAnterior) {
                    pilhaEstados.addLast(i);
                }
            }
        }
    }

    public List<Erro> getErros() {
        return erros;
    }
}
