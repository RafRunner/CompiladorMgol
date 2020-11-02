package partesCompilador.analisadorSintatico.tabela;

import dominio.LeitorArquivos;
import dominio.enums.Token;
import partesCompilador.analisadorSintatico.NaoTerminal;
import partesCompilador.analisadorSintatico.RegraGramatical;

import java.io.FileNotFoundException;
import java.util.List;

public class TabelaSintatica {

    private static final String caminhoTabelaAction = "res/analisadorSintatico/action.csv";
    private static final String caminhoTabelaGoto = "res/analisadorSintatico/goto.csv";
    private final List<List<String>> tabelaAction;
    private final List<List<String>> tabelaGoto;

    public TabelaSintatica() {
        try {
            this.tabelaAction = LeitorArquivos.lerCsv(caminhoTabelaAction);
            this.tabelaGoto = LeitorArquivos.lerCsv(caminhoTabelaGoto);

        } catch (FileNotFoundException ignored) {
            // Nunca deve acontecer
            throw new RuntimeException("Caminho de algum dos arquivos da tabela sintática errado");
        }
    }

    public Action action(final Integer estado, final Token token) {
        final String stringAction = tabelaAction.get(estado + 1).get(token.ordinal() + 1);

        // Erro
        if (stringAction.equals(" ")) {
            return null;
        }

        if (stringAction.equals("ACC")) {
            return new Accept();
        }

        if (stringAction.startsWith("S")) {
            return new Shift(Integer.parseInt(stringAction.substring(1)));
        }
        return new Reduce(RegraGramatical.values()[Integer.parseInt(stringAction.substring(1)) - 1]);
    }

    public Integer Goto(final Integer estado, final NaoTerminal naoTerminal) {
        final String stringGoto = tabelaGoto.get(estado + 1).get(naoTerminal.ordinal());
        return Integer.parseInt(stringGoto);
    }
}
