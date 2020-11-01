package partesCompilador.analisadorSintatico.tabela;

import partesCompilador.analisadorSintatico.RegraGramatical;

public class Reduce extends Action {

    private final RegraGramatical regraGramatical;

    public Reduce(RegraGramatical regraGramatical) {
        this.regraGramatical = regraGramatical;
    }

    public RegraGramatical getRegraGramatical() {
        return regraGramatical;
    }
}
