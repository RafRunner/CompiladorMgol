package partesCompilador.analisadorSintatico;

import dominio.Analisador;
import dominio.TokenEAtributos;
import dominio.TokenLocalizado;
import dominio.enums.Cor;
import dominio.enums.Token;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSintatico.tabela.*;

import java.util.*;

public class AnalisadorSintatico extends Analisador {

    private final Deque<Integer> pilhaEstados = new ArrayDeque<>();
    private final TabelaSintatica tabelaSintatica = new TabelaSintatica();
    private final AnalisadorLexico analisadorLexico;

    public AnalisadorSintatico(final AnalisadorLexico analisadorLexico, final int verbosidade) {
        super(analisadorLexico.getErros(), verbosidade, Cor.CYAN);
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

            // Se não é nenhuma das possibildiades acima, sobrou apenas o erro
            else {
                final ErroSintatico erro = (ErroSintatico) action;

                final TokenLocalizado tokenUsado = erro.usaAtual() ? tokenAtual : tokenAnterior;

                // Entrando na recuperação de erro genérica (modo de pânico por linha)
                if (erro.getTipo().ordinal() <= TipoErro.E16.ordinal()) {
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

                    if (tokenAtual.getToken() == Token.eof) {
                        imprimeAviso("Não foi possível terminar a derivação gramatical do programa devido a erros. Análise será limitada");
                        break;
                    }
                }

                // No caso de um id solto, simulamos uma atribuição a ele mesmo
                else if (erro.getTipo() == TipoErro.E9) {
                    analisadorLexico.pushToBacklog(tokenAtual);
                    analisadorLexico.pushToBacklog(tokenAnterior);
                    tokenAtual = Token.rcb.darAtributos("<-").localizar();
                }

                // Argumento inválido para leia/escreva. Simulamos um id qualquer (pegado da tabela de símbolos)
                else if (erro.getTipo() == TipoErro.E10) {
                    final Optional<TokenEAtributos> idQualquer = analisadorLexico.getTabelaDeSimbolos().values().stream().filter(token -> token.getToken() == Token.id).findFirst();
                    tokenAtual = idQualquer.orElse(Token.id.darAtributos("EU_NAO_EXISTO")).localizar();
                }

                // Caso onde falta um opm ou opr. Nesse caso sempre sabemos que vamos fazer a redução pela regra 20 ou 21, então forçamos essa redução
                // e depois verificamos a action com opm. Se for erro sabemos que deveria ser um opr, caso contrário deixamos opr.
                else if (erro.getTipo() == TipoErro.E13) {
                    final Integer temp = pilhaEstados.pop();
                    analisadorLexico.pushToBacklog(tokenAtual);

                    final Integer estadoAposReducao = tabelaSintatica.Goto(pilhaEstados.peek(), NaoTerminal.OPRD);
                    final TokenEAtributos token = tabelaSintatica.action(estadoAposReducao, Token.opm) instanceof ErroSintatico ?
                            Token.opr.darAtributos(">") : Token.opm.darAtributos("+");

                    tokenAtual = token.localizar();
                    pilhaEstados.push(temp);

                    final String mensagem = token.getToken() == Token.opm ?
                            "Fatando operador matemático após \"" + tokenUsado.getLexema() + "\"" :
                            "Fatando operador lógico após \"" + tokenUsado.getLexema() + "\"";

                    criaRegistraEImprimeErro(mensagem, tokenUsado.getLinha(), tokenUsado.getColuna());
                    continue;
                }

                // Tratamento comum para todos os casos onde assumimos que um token está faltando
                else {
                    analisadorLexico.pushToBacklog(tokenAtual);
                    tokenAtual = erro.getTipo().getTokenFaltando().localizar();
                }

                // Caso seja um token de erro, é um erro léxico e não deve ser registrado novamente
                if (tokenUsado.getToken() != Token.erro) {
                    criaRegistraEImprimeErro("Erro sintático: " + erro.montaDetalhe(tokenUsado), tokenUsado.getLinha(), tokenUsado.getColuna());
                }
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
