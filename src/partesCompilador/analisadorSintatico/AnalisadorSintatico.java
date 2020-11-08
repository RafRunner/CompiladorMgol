package partesCompilador.analisadorSintatico;

import dominio.Analisador;
import dominio.TokenEAtributos;
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
        TokenLocalizado tokenAnterior = tokenAtual;

        // A pilha começa com o estado 0
        pilhaEstados.push(0);
        pilhaLinhaAnterior.push(0);

        while (true) {
            final Integer estado = pilhaEstados.peek();
            final Action action = tabelaSintatica.action(estado, tokenAtual.getToken());

            if (action instanceof ErroSintatico) {
                final ErroSintatico erro = (ErroSintatico) action;

                // Entrando na recuperação de erro genérica (modo de pânico por linha)
                if (erro.getTipo() == TipoErro.E0) {
                    if (tokenAtual.getToken() != Token.erro) {
                        criaRegistraEImprimeErro("Erro sintático: " + erro.montaDetalhe(tokenAtual), tokenAtual.getLinha(), tokenAtual.getColuna());
                    }

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

                // No caso de um id solto, simulamos uma atribuição a ele mesmo
                else if (erro.getTipo() == TipoErro.E9) {
                    criaRegistraEImprimeErro("Erro sintático: " + erro.montaDetalhe(tokenAnterior), tokenAnterior.getLinha(), tokenAnterior.getColuna());
                    analisadorLexico.pushToBacklog(tokenAtual);
                    analisadorLexico.pushToBacklog(Token.pt_v.darAtributos(";").localizar(-1, -1));
                    analisadorLexico.pushToBacklog(tokenAnterior.getTokenEAtributos().localizar(-1, -1));
                    tokenAtual = Token.rcb.darAtributos("<-").localizar(-1, -1);
                }

                // Argumento inválido para leia/escreva. Simulamos um id qualquer (pegado da tabela de símbolos)
                else if (erro.getTipo() == TipoErro.E10) {
                    criaRegistraEImprimeErro("Erro sintático: " + erro.montaDetalhe(tokenAtual), tokenAnterior.getLinha(), tokenAnterior.getColuna());
                    analisadorLexico.pushToBacklog(tokenAtual);
                    final Optional<TokenEAtributos> idQualquer = analisadorLexico.getTabelaDeSimbolos().values().stream().filter(token -> token.getToken() == Token.id).findFirst();
                    tokenAtual = idQualquer.orElse(Token.id.darAtributos("EU_NAO_EXISTO")).localizar(-1, -1);
                }

                // Tratamento comum para todos os casos onde assumimos que um token está faltando
                else {
                    criaRegistraEImprimeErro("Erro sintático: " + erro.montaDetalhe(tokenAnterior), tokenAnterior.getLinha(), tokenAnterior.getColuna());
                    analisadorLexico.pushToBacklog(tokenAtual);
                    tokenAtual = erro.getTipo().getTokenFaltando().localizar(tokenAnterior.getLinha(), tokenAnterior.getColuna());
                }

                if (tokenAtual.getToken() == Token.eof) {
                    imprimeAviso("Não foi possível terminar a derivação gramatical do programa devido a erros. Análise será limitada");
                    break;
                }
            }

            if (action instanceof Shift) {
                final Integer novoEstado = ((Shift) action).getEstado();
                pilhaEstados.push(novoEstado);

                tokenAnterior = tokenAtual;
                tokenAtual = analisadorLexico.lerProximoTokenNaoComentario();

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
