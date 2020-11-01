package partesCompilador.analisadorSintatico;

import dominio.TokenLocalizado;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSintatico.tabela.*;

import java.util.ArrayDeque;
import java.util.Deque;

public class AnalisadorSintatico {

    private final Deque<Integer> pilhaEstados = new ArrayDeque<>();
    final private AnalisadorLexico analisadorLexico;
    final private TabelaSintatica tabelaSintatica;

    public AnalisadorSintatico(final AnalisadorLexico analisadorLexico) {
        this.analisadorLexico = analisadorLexico;
        this.tabelaSintatica = new TabelaSintatica();

        // A pilha começa com o estado 0
        pilhaEstados.push(0);
    }

    public void analisa() {
        TokenLocalizado tokenAtual = analisadorLexico.lerProximoToken();

        while (true) {
            final Integer estado = pilhaEstados.peek();
            final Action action = tabelaSintatica.action(estado, tokenAtual.getToken());

            if (action instanceof Shift) {
                final Integer novoEstado = ((Shift) action).getEstado();

                pilhaEstados.push(novoEstado);
                tokenAtual = analisadorLexico.lerProximoToken();
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
                break;
            }

            // Erro! Entrando na recuperação de erro
            else {
                System.out.println("Rotina de erro não implementada!");
                break;
            }
        }
    }
}
