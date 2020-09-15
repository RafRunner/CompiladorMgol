package testes;

import dominio.DFALexico;
import dominio.excecoes.EstadoDeErroException;

public class TestesAnalisadorLexico {

    public static void main(String[] args) {
        final var DFA = new DFALexico();

        try {
            DFA.aplicarFuncaoTrasicao('d');
            DFA.aplicarFuncaoTrasicao('e');
        } catch (EstadoDeErroException e) {
            e.printStackTrace();
        }
    }
}
