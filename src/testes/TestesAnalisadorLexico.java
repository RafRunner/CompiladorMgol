package testes;

import dominio.DFAMgol;
import dominio.enums.Token;
import dominio.excecoes.EstadoDeErroException;

public class TestesAnalisadorLexico {

    public static void main(String[] args) {
        final var DFA = new DFAMgol();

        try {
            DFA.aplicarFuncaoTrasicao('d');
            DFA.aplicarFuncaoTrasicao('e');
        } catch (EstadoDeErroException e) {
            e.printStackTrace();
        }
    }
}
