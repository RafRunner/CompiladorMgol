package testes;

import dominio.DFALexico;
import dominio.enums.Token;
import dominio.excecoes.EstadoDeErroException;
import dominio.excecoes.FimDeTokenValidoException;

import java.util.stream.Stream;

public class TestesAnalisadorLexico {

    private static void aplicaSequenciaAoDFA(final DFALexico DFA, final String sequencia) throws EstadoDeErroException, FimDeTokenValidoException {
        DFA.resetar();
        for (final char c : sequencia.toCharArray()) {
            DFA.aplicarFuncaoTrasicao(c);
        }
    }

    public static void main(String[] args) throws Exception {
        final var DFA = new DFALexico();

        System.out.println("-----------------------------------Testes do DFA------------------------------------------------");
        System.out.println("Testando leitura de tokens esperados. Caso o resultado de um seja false, o teste falhou\n");

        aplicaSequenciaAoDFA(DFA, "identificador");
        System.out.println("1 - " + (DFA.getEstado().getTokenAssociado() == Token.id));

        aplicaSequenciaAoDFA(DFA, "{&8*% 43__\n comentário }");
        System.out.println("2 - " + (DFA.getEstado().getTokenAssociado() == Token.Comentario));

        aplicaSequenciaAoDFA(DFA, "\"Isso é uma string _ %&*&%# 34e+23 \\n\\t\\\\\"");
        System.out.println("3 - " + (DFA.getEstado().getTokenAssociado() == Token.Literal));

        aplicaSequenciaAoDFA(DFA, "23");
        System.out.println("4 - " + (DFA.getEstado().getTokenAssociado() == Token.NUM));

        aplicaSequenciaAoDFA(DFA, "321.32");
        System.out.println("5 - " + (DFA.getEstado().getTokenAssociado() == Token.NUM));

        aplicaSequenciaAoDFA(DFA, "231e2");
        System.out.println("6 - " + (DFA.getEstado().getTokenAssociado() == Token.NUM));

        aplicaSequenciaAoDFA(DFA, "231e-2");
        System.out.println("7 - " + (DFA.getEstado().getTokenAssociado() == Token.NUM));

        aplicaSequenciaAoDFA(DFA, "231.32e10");
        System.out.println("8 - " + (DFA.getEstado().getTokenAssociado() == Token.NUM));

        aplicaSequenciaAoDFA(DFA, "idetificador_com_underscore_23");
        System.out.println("9 - " + (DFA.getEstado().getTokenAssociado() == Token.id));

        aplicaSequenciaAoDFA(DFA, ")");
        System.out.println("10 - " + (DFA.getEstado().getTokenAssociado() == Token.AB_P));

        aplicaSequenciaAoDFA(DFA, "(");
        System.out.println("11 - " + (DFA.getEstado().getTokenAssociado() == Token.FC_P));

        aplicaSequenciaAoDFA(DFA, ";");
        System.out.println("12 - " + (DFA.getEstado().getTokenAssociado() == Token.PT_V));

        aplicaSequenciaAoDFA(DFA, "=");
        System.out.println("13 - " + (DFA.getEstado().getTokenAssociado() == Token.OPR));

        aplicaSequenciaAoDFA(DFA, ">=");
        System.out.println("14 - " + (DFA.getEstado().getTokenAssociado() == Token.OPR));

        aplicaSequenciaAoDFA(DFA, "<=");
        System.out.println("15 - " + (DFA.getEstado().getTokenAssociado() == Token.OPR));

        aplicaSequenciaAoDFA(DFA, "<>");
        System.out.println("16 - " + (DFA.getEstado().getTokenAssociado() == Token.OPR));

        aplicaSequenciaAoDFA(DFA, ">");
        System.out.println("17 - " + (DFA.getEstado().getTokenAssociado() == Token.OPR));

        aplicaSequenciaAoDFA(DFA, "<");
        System.out.println("18 - " + (DFA.getEstado().getTokenAssociado() == Token.OPR));

        aplicaSequenciaAoDFA(DFA, "<-");
        System.out.println("19 - " + (DFA.getEstado().getTokenAssociado() == Token.RCB));

        System.out.println("\nTestando leitura de tokens inesperados. Caso o resultado de um teste não seja impresso, o teste falhou\n");

        try {
            aplicaSequenciaAoDFA(DFA, "identificador espaco");
        } catch (FimDeTokenValidoException e) {
            System.out.println("1 - " + e.getMessage());
        }

        try {
            aplicaSequenciaAoDFA(DFA, "=-");
        } catch (FimDeTokenValidoException e) {
            System.out.println("2 - " + e.getMessage());
        }

        try {
            aplicaSequenciaAoDFA(DFA, "32.e1");
        } catch (EstadoDeErroException e) {
            System.out.println("3 - " + e.getMessage());
        }

        try {
            aplicaSequenciaAoDFA(DFA, "321e4e1");
        } catch (FimDeTokenValidoException e) {
            System.out.println("4 - " + e.getMessage());
        }

        try {
            aplicaSequenciaAoDFA(DFA, "\"string com \n quebra de linha\"");
        } catch (EstadoDeErroException e) {
            System.out.println("5 - " + e.getMessage());
        }

        try {
            aplicaSequenciaAoDFA(DFA, "2.d");
        } catch (EstadoDeErroException e) {
            System.out.println("6 - " + e.getMessage());
        }

        try {
            aplicaSequenciaAoDFA(DFA, "9et");
        } catch (EstadoDeErroException e) {
            System.out.println("7 - " + e.getMessage());
        }

        try {
            aplicaSequenciaAoDFA(DFA, "\"Sequência de escape inválida: \\y\"");
        } catch (EstadoDeErroException e) {
            System.out.println("8 - " + e.getMessage());
        }
    }
}
