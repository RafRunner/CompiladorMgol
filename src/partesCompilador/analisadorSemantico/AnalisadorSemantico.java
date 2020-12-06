package partesCompilador.analisadorSemantico;

import dominio.*;
import dominio.enums.Cor;
import dominio.enums.Token;
import partesCompilador.analisadorSintatico.RegraGramatical;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class AnalisadorSemantico extends Analisador {

    private final Deque<Object> pilhaSemantica = new ArrayDeque<>();
    private final BufferedWriter output;

    public AnalisadorSemantico(final List<Erro> erros, final BufferedWriter output, final int verbosidade) {
        super(erros, verbosidade, Cor.GREEN);
        this.output = output;
    }

    public void empilhaToken(final TokenLocalizado token) {
        pilhaSemantica.push(token);
    }

    public void aplicaRegraSemantica(final RegraGramatical regraGramatical) throws IOException {

        final NaoTerminalEAtributos ladoEsquesdo = regraGramatical.getLadoEsquedo().darAtributos();
        final List<Object> ladoDireito = regraGramatical.getLadoDireito();

        final List<Object> ladoDireitoContextualizado = new ArrayList<>();

        for (int i = 0; i < ladoDireito.size(); i++) {
            ladoDireitoContextualizado.add(pilhaSemantica.pop());
        }
        Collections.reverse(ladoDireitoContextualizado);

        try {
            RegraSemantica.values()[regraGramatical.ordinal()].aplicar(ladoEsquesdo, ladoDireitoContextualizado, output);
        } catch (ErroSemanticoException e) {
            criaRegistraEImprimeErro(e.getMensagem(), e.getLinha(), e.getColuna());
        }

        pilhaSemantica.push(ladoEsquesdo);
    }

    public void fechaArquivo() throws IOException {
        output.close();
    }
}
