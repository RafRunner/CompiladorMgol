package partesCompilador.analisadorSemantico;

import dominio.NaoTerminalEAtributos;
import dominio.TokenLocalizado;
import dominio.enums.Tipo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

enum RegraSemantica {

    R1((ladoEsquerdo, ladoDireito, output) -> { }),

    R2((ladoEsquerdo, ladoDireito, output) -> { }),

    R3((ladoEsquerdo, ladoDireito, output) -> { }),

    R4((ladoEsquerdo, ladoDireito, output) -> { }),

    R5((ladoEsquerdo, ladoDireito, output) -> {
        output.append("\n\n\n");
    }),

    R6((ladoEsquerdo, ladoDireito, output) -> {
        final var id = (TokenLocalizado) ladoDireito.get(0);
        final var TIPO = (NaoTerminalEAtributos) ladoDireito.get(1);

        id.setTipo(TIPO.getAtributos().getTipo());

        output.append(TIPO.getTipo().toString()).append(" ").append(id.getLexema()).append(";\n");
    }),

    R7((ladoEsquerdo, ladoDireito, output) -> {
        // Lado esquerdo = TIPO
        ladoEsquerdo.getAtributos().setTipo(Tipo.INTEIRO);
    }),

    R8((ladoEsquerdo, ladoDireito, output) -> {
        // Lado esquerdo = TIPO
        ladoEsquerdo.getAtributos().setTipo(Tipo.REAL);
    }),

    R9((ladoEsquerdo, ladoDireito, output) -> {
        // Lado esquerdo = TIPO
        ladoEsquerdo.getAtributos().setTipo(Tipo.LITERAL);
    }),

    R10((ladoEsquerdo, ladoDireito, output) -> { }),

    R11((ladoEsquerdo, ladoDireito, output) -> {
        final var id = (TokenLocalizado) ladoDireito.get(0);

        if (id.getTipo() != null) {
            switch (id.getTipo()) {
                case LITERAL: output.append("scanf(\"%s\", "); break;
                case INTEIRO: output.append("scanf(\"%d\", &"); break;
                case REAL: output.append("scanf(\"%f\", &");
            }
            output.append(id.getLexema()).append(");");
        }
        else {
            throw new ErroSemanticoException("Variável " + id.getLexema() + " não declarada.", id.getLinha(), id.getColuna());
        }
    }),

    R12((ladoEsquerdo, ladoDireito, output) -> {
        final var ARG = (NaoTerminalEAtributos) ladoDireito.get(1);

        switch (ARG.getTipo()) {
            case LITERAL: output.append("printf(").append(ARG.getLexema()); break;
            case INTEIRO: output.append("printf(\"%d\", ").append(ARG.getLexema()); break;
            case REAL: output.append("printf(\"%f\", ").append(ARG.getLexema()); break;
        }

        output.append(");\n");
    }),

    R13((ladoEsquerdo, ladoDireito, output) -> {
        // ladoEsquerdo = ARG
        final var literal = (TokenLocalizado) ladoDireito.get(0);
        
        ladoEsquerdo.setAtributos(literal.getTokenEAtributos());
    }),

    R14((ladoEsquerdo, ladoDireito, output) -> {
        // ladoEsquerdo = ARG
        final var num = (TokenLocalizado) ladoDireito.get(0);

        ladoEsquerdo.setAtributos(num.getTokenEAtributos());
    }),

    R15((ladoEsquerdo, ladoDireito, output) -> {
        // ladoEsquerdo = ARG
        final var id = (TokenLocalizado) ladoDireito.get(0);

        ladoEsquerdo.setAtributos(id.getTokenEAtributos());
    }),

    R16((ladoEsquerdo, ladoDireito, output) -> { }),

    R17((ladoEsquerdo, ladoDireito, output) -> {
        final var id = (TokenLocalizado) ladoDireito.get(0);
        final var LD = (NaoTerminalEAtributos) ladoDireito.get(2);

        if (id.getTipo() != null) {
            if (id.getTipo() == LD.getTipo()) {
                output.append(id.getLexema()).append("=").append(LD.getLexema());
            }
            else {
                throw new ErroSemanticoException("Variável " + id.getLexema() + " é do tipo " + id.getTipo() + ", não " +LD.getTipo(), id.getLinha(), id.getColuna());
            }
        }
        else {
            throw new ErroSemanticoException("Variável " + id.getLexema() + " não declarada.", id.getLinha(), id.getColuna());
        }
    }),

    R18((ladoEsquerdo, ladoDireito, output) -> {
        final var OPRD1 = (NaoTerminalEAtributos) ladoDireito.get(0);
        final var opm = (TokenLocalizado) ladoDireito.get(1);
        final var OPRD2 = (NaoTerminalEAtributos) ladoDireito.get(2);

        if (OPRD1.getTipo() == OPRD2.getTipo() && OPRD1.getTipo() != Tipo.LITERAL) {

        }
        else {
            throw new ErroSemanticoException("Operados com tipos incompatíveis " + OPRD1.getTipo() + " e " + OPRD2.getTipo(), opm.getLinha(), opm.getColuna());
        }
    }),

    R19((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R20((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R21((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R22((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R23((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R24((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R25((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R26((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R27((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R28((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R29((ladoEsquerdo, ladoDireito, output) -> {

    }),

    R30((ladoEsquerdo, ladoDireito, output) -> {

    });

    private interface Regra {
        void aplicar(final NaoTerminalEAtributos ladoEsquerdo,
                     final List<Object> ladoDireitoContextualizado,
                     final BufferedWriter output) throws IOException, ErroSemanticoException;
    }

    private final Regra regra;

    RegraSemantica(final Regra regra) {
        this.regra = regra;
    }

    public void aplicar(final NaoTerminalEAtributos ladoEsquerdo,
                        final List<Object> ladoDireitoContextualizado,
                        final BufferedWriter output) throws IOException, ErroSemanticoException {
        this.regra.aplicar(ladoEsquerdo, ladoDireitoContextualizado, output);
    }
}
