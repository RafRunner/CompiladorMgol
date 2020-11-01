package partesCompilador.analisadorSintatico.tabela;

public class Shift extends Action {

    private final Integer estado;

    public Shift(Integer estado) {
        this.estado = estado;
    }

    public Integer getEstado() {
        return estado;
    }
}
