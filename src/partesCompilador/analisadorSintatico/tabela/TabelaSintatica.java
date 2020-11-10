package partesCompilador.analisadorSintatico.tabela;

import dominio.LeitorArquivos;
import dominio.enums.Cor;
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
            throw new RuntimeException("Caminho de algum dos arquivos da tabela sintática errado. Devem estar no mesmo diretório do executável em res/analisadorSintatico");
        }
    }

    public Action action(final Integer estado, final Token token) {
        final String stringAction = tabelaAction.get(estado + 1).get(token.ordinal() + 1);

        if (stringAction.startsWith("S")) {
            return new Shift(Integer.parseInt(stringAction.substring(1)));
        }

        if (stringAction.startsWith("R")) {
            return new Reduce(RegraGramatical.values()[Integer.parseInt(stringAction.substring(1)) - 1]);
        }

        if (stringAction.equals("ACC")) {
            return new Accept();
        }

        //Cor.imprimeComCor("Debuggando: S" + estado + " token: " + token, Cor.GREEN);
        if (stringAction.equals(" ")) {
            return new ErroSintatico(TipoErro.E0);
        }

        return new ErroSintatico(TipoErro.valueOf(stringAction));
    }

    public Integer Goto(final Integer estado, final NaoTerminal naoTerminal) {
        final String stringGoto = tabelaGoto.get(estado + 1).get(naoTerminal.ordinal());
        return Integer.parseInt(stringGoto);
    }
}
