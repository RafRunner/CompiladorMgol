package partesCompilador.analisadorSemantico;

import dominio.*;
import dominio.enums.Cor;
import dominio.enums.Token;
import partesCompilador.analisadorSintatico.RegraGramatical;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class AnalisadorSemantico extends Analisador {

    private final List<TokenLocalizado> pilhaTokens = new ArrayList<>();
    private final Deque<NaoTerminalEAtributos> pilhaNaoTerminais = new ArrayDeque<>();
    private final BufferedWriter output;

    public AnalisadorSemantico(final List<Erro> erros, final BufferedWriter output, final int verbosidade) {
        super(erros, verbosidade, Cor.GREEN);
        this.output = output;
    }

    public void empilhaToken(final TokenLocalizado token) {
        pilhaTokens.add(0, token);
    }

    public void empilhaNaoTermina(final NaoTerminalEAtributos naoTerminal) {
        pilhaNaoTerminais.push(naoTerminal);
    }

    public void aplicaRegraSemantica(final RegraGramatical regraGramatical) throws IOException {

        final NaoTerminalEAtributos ladoEsquesdo = regraGramatical.getLadoEsquedo().darAtributos();
        final List<Object> ladoDireito = regraGramatical.getLadoDireito();

        final List<Object> ladoDireitoContextualizado = new ArrayList<>();

        for (final Object o : ladoDireito) {
            if (o instanceof Token) {
                ladoDireitoContextualizado.add(pilhaTokens.remove(2));
            }
            else {
                ladoDireitoContextualizado.add(pilhaNaoTerminais.pop());
            }
        }

        try {
            RegraSemantica.values()[regraGramatical.ordinal()].aplicar(ladoEsquesdo, ladoDireitoContextualizado, output);
        } catch (ErroSemanticoException e) {
            criaRegistraEImprimeErro(e.getMensagem(), e.getLinha(), e.getColuna());
        }

        pilhaNaoTerminais.push(ladoEsquesdo);
    }

    public void fechaArquivo() throws IOException {
        output.close();
    }
}
