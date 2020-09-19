package dominio.interfaces;

import dominio.enums.EstadoDFALexico;
import dominio.excecoes.EstadoDeErroException;
import dominio.excecoes.FimDeTokenValidoException;

// Interface que modela uma função que leva de um estado a outro através de um caractere. Caso leia um caracter que não leva a outro estado,
// lança a EstadoDeErroException com detalhes do erro ou FimDeTokenValidoException com o tipo de token gerado
public interface FuncaoTransicao {
    EstadoDFALexico aplicar(final char caractere) throws EstadoDeErroException, FimDeTokenValidoException;
}
