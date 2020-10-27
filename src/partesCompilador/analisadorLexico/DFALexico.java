package partesCompilador.analisadorLexico;

import partesCompilador.analisadorLexico.excecoes.EstadoDeErroException;
import partesCompilador.analisadorLexico.excecoes.FimDeTokenValidoException;

// Classe que representa o DFA léxico, guarda estado e é capaz de ler caracteres
public class DFALexico {

    public static final EstadoDFALexico ESTADO_INICIAL = EstadoDFALexico.S0;

    private EstadoDFALexico estado = ESTADO_INICIAL;

    public void aplicarFuncaoTrasicao(final char caractere) throws EstadoDeErroException, FimDeTokenValidoException {
        estado = estado.aplicarFuncaoTrasicao(caractere);
    }

    public EstadoDFALexico getEstado() {
        return estado;
    }

    public void resetar() {
        estado = ESTADO_INICIAL;
    }
}
