package dominio.interfaces;

import dominio.enums.EstadoDFALexico;
import dominio.excecoes.EstadoDeErroException;
import dominio.excecoes.FimDeTokenValidoException;

public interface FuncaoTransicao {
    EstadoDFALexico aplicar(final char caractere) throws EstadoDeErroException, FimDeTokenValidoException;
}
