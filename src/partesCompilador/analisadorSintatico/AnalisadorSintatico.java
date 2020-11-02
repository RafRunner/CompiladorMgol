package partesCompilador.analisadorSintatico;

import dominio.TokenLocalizado;
import dominio.enums.Token;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSintatico.tabela.*;

import java.util.ArrayDeque;
import java.util.Deque;

public class AnalisadorSintatico {

    private final Deque<Integer> pilhaEstados = new ArrayDeque<>();
    final private TabelaSintatica tabelaSintatica = new TabelaSintatica();
    final private AnalisadorLexico analisadorLexico;

    public AnalisadorSintatico(final AnalisadorLexico analisadorLexico) {
        this.analisadorLexico = analisadorLexico;

        // A pilha começa com o estado 0
        pilhaEstados.push(0);
    }

    public void analisa() {
        TokenLocalizado tokenAtual = analisadorLexico.lerProximoTokenNaoComentario();

        while (true) {
            final Integer estado = pilhaEstados.peek();
            final Action action = tabelaSintatica.action(estado, tokenAtual.getToken());

            if (action instanceof Shift) {
                final Integer novoEstado = ((Shift) action).getEstado();

                pilhaEstados.push(novoEstado);
                tokenAtual = analisadorLexico.lerProximoTokenNaoComentario();
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
                    System.out.printf("\nErro sintático em: \"%s\" próximo da linha %d coluna %d\n",
                            tokenAtual.getLexema(), tokenAtual.getLinha() + 1, tokenAtual.getColuna());
                }

                break;
            }
        }
    }
}
