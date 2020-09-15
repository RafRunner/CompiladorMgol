package dominio;

import dominio.enums.EstadoDFALexico;
import dominio.excecoes.EstadoDeErroException;

// Classe que representa o DFA léxico, guarda estado e é capaz de ler caracteres
public class DFALexico {

    private EstadoDFALexico estado = EstadoDFALexico.S0;

    public EstadoDFALexico aplicarFuncaoTrasicao(final char caractere) throws EstadoDeErroException {
        estado = estado.aplicarFuncaoTrasicao(caractere);
        return estado;
    }

    public void resetar() {
        estado = EstadoDFALexico.S0;
    }
}
