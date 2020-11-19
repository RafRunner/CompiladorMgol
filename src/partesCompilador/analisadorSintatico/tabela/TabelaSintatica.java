package partesCompilador.analisadorSintatico.tabela;

import dominio.LeitorArquivos;
import dominio.enums.Cor;
import dominio.enums.Token;
import partesCompilador.analisadorSintatico.NaoTerminal;
import partesCompilador.analisadorSintatico.RegraGramatical;

import java.io.FileNotFoundException;
import java.util.List;

public class TabelaSintatica {

    private static final String caminhoTabelaAction = "/analisadorSintatico/action.csv";
    private static final String caminhoTabelaGoto = "/analisadorSintatico/goto.csv";
    private final List<List<String>> tabelaAction;
    private final List<List<String>> tabelaGoto;

    public TabelaSintatica() {
        try {
            this.tabelaAction = LeitorArquivos.lerCsvResource(caminhoTabelaAction);
            this.tabelaGoto = LeitorArquivos.lerCsvResource(caminhoTabelaGoto);

        } catch (final FileNotFoundException e) {
            // Nunca deve acontecer
            throw new RuntimeException("Um dos arquivos das tabelas não foi encontrado: " + e);
        }
    }

    public Action action(final Integer estado, final Token token) {
        final String stringAction = tabelaAction.get(estado + 1).get(token.ordinal() + 1);

        if (stringAction.startsWith("S")) {
            return new Shift(Integer.parseInt(stringAction.substring(1)));
        }

        if (stringAction.startsWith("R")) {
            return new Reduce(RegraGramatical.valueOf(stringAction));
        }

        if (stringAction.equals("ACC")) {
            return new Accept();
        }

        //Cor.imprimeComCor("Debuggando: S" + estado + " token: " + token, Cor.GREEN);
        if (stringAction.startsWith("E")) {
            return new ErroSintatico(TipoErro.valueOf(stringAction));
        }

        return new ErroSintatico(TipoErro.E0);
    }

    public Integer Goto(final Integer estado, final NaoTerminal naoTerminal) {
        final String stringGoto = tabelaGoto.get(estado + 1).get(naoTerminal.ordinal());
        return Integer.parseInt(stringGoto);
    }
}
