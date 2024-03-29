package partesCompilador.analisadorSemantico;

import dominio.*;
import dominio.enums.Cor;
import partesCompilador.analisadorSintatico.RegraGramatical;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class AnalisadorSemantico extends Analisador {

    private final Deque<Object> pilhaSemantica = new ArrayDeque<>();
    private final StringBuilder output = new StringBuilder();
    private final List<String> variaveisTemporarias = new ArrayList<>();

    private boolean erroNaGeracao = false;

    public AnalisadorSemantico(final List<Erro> erros, final int verbosidade) {
        super(erros, verbosidade, Cor.GREEN);
    }

    public void empilhaToken(final TokenLocalizado token) {
        if (erroNaGeracao) return;
        pilhaSemantica.push(token);
    }

    public void aplicaRegraSemantica(final RegraGramatical regraGramatical) {
        if (erroNaGeracao) return;

        final NaoTerminalEAtributos ladoEsquerdo = regraGramatical.getLadoEsquedo().darAtributos();
        final List<Object> ladoDireito = regraGramatical.getLadoDireito();

        final List<Object> ladoDireitoContextualizado = new ArrayList<>();

        for (int i = 0; i < ladoDireito.size(); i++) {
            ladoDireitoContextualizado.add(0, pilhaSemantica.pop());
        }

        try {
            RegraSemantica.values()[regraGramatical.ordinal()].aplicar(ladoEsquerdo, ladoDireitoContextualizado, variaveisTemporarias, output);
        } catch (ErroSemanticoException e) {
            criaRegistraEImprimeErro("Erro semântico: " + e.getMensagem(), e.getLinha(), e.getColuna());
        } catch (Exception ignored) {
            // Caso um erro inesperado occorra na geração de código, não será possível mais gerar o programa objeto e marcamos essa flag
            erroNaGeracao = true;
            return;
        }

        pilhaSemantica.push(ladoEsquerdo);
    }

    public void escreveArquivo(final BufferedWriter arqSaida) throws IOException {
        var cabecalho = new StringBuilder();
        cabecalho.append("#include <stdio.h>\n")
                .append("\ntypedef char literal[256];\n\n")
                .append("int main() {\n")
                .append("/*----Variaveis temporarias----*/\n");

        for (var variavelTemporaria : variaveisTemporarias) {
            cabecalho.append(variavelTemporaria);
        }

        cabecalho.append("/*------------------------------*/\n");

        arqSaida.append(cabecalho);
        arqSaida.append(output);
        arqSaida.close();
    }
}
