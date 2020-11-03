package partesCompilador.analisadorSintatico;

import dominio.Erro;
import dominio.TokenLocalizado;
import dominio.enums.Token;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSintatico.tabela.*;

import java.util.*;

public class AnalisadorSintatico {

    private final Deque<Integer> pilhaEstados = new ArrayDeque<>();
    private final TabelaSintatica tabelaSintatica = new TabelaSintatica();
    private final AnalisadorLexico analisadorLexico;

    private final List<Erro> erros;

    public AnalisadorSintatico(final AnalisadorLexico analisadorLexico) {
        this.analisadorLexico = analisadorLexico;
        this.erros = analisadorLexico.getErros();
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
                    for (Integer i : pilhaEstados) {
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

                System.out.println(regra);
            }

            else if (action instanceof Accept) {
                System.out.println(RegraGramatical.r01);
                break;
            }

            // Erro! Entrando na recuperação de erro
            else {
                if (tokenAtual.getToken() != Token.erro) {
                    final Erro erro = new Erro(
                            String.format("Erro sintático: Token do tipo \"%s\" inesperado: %s", tokenAtual.getToken(), tokenAtual.getLexema()),
                            tokenAtual.getLinha() + 1, tokenAtual.getColuna());
                    System.out.println("\n" + erro + "\n");
                    erros.add(erro);
                }

                // Vamos ignorar tokens até chegar na próxima linha e então restaurar a pilha
                TokenLocalizado tokenIgnorado = analisadorLexico.lerProximoTokenNaoComentario();
                while(tokenIgnorado.getLinha() == linhaAtual) {
                    tokenIgnorado = analisadorLexico.lerProximoTokenNaoComentario();
                }

                // O token a ser analisado é o primeiro da nova linha
                tokenAtual = tokenIgnorado;
                // Atualizando a linha
                linhaAtual = tokenAtual.getLinha();
                // Restaurando a pilhaDeEstados para como estava na linha anterior (antes dos erros)
                pilhaEstados.clear();
                for (Integer i : pilhaLinhaAnterior) {
                    pilhaEstados.addLast(i);
                }
            }
        }
    }

    public List<Erro> getErros() {
        return erros;
    }
}
