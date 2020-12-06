package partesCompilador.analisadorSemantico;

import dominio.NaoTerminalEAtributos;
import dominio.TokenLocalizado;
import dominio.enums.Tipo;

import java.io.IOException;
import java.util.List;

enum RegraSemantica {

    R1(),

    R2((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        output.append("\nreturn 0;\n}\n");
    }),

    R3(),

    R4(),

    R5((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        output.append("\n\n\n");
    }),

    R6((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var id = (TokenLocalizado) ladoDireito.get(0);
        final var TIPO = (NaoTerminalEAtributos) ladoDireito.get(1);

        id.setTipo(TIPO.getAtributos().getTipo());

        output.append(TIPO.getTipo().toString()).append(" ").append(id.getLexema()).append(";\n");
    }),

    R7((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        // Lado esquerdo = TIPO
        ladoEsquerdo.getAtributos().setTipo(Tipo.INTEIRO);
    }),

    R8((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        // Lado esquerdo = TIPO
        ladoEsquerdo.getAtributos().setTipo(Tipo.REAL);
    }),

    R9((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        // Lado esquerdo = TIPO
        ladoEsquerdo.getAtributos().setTipo(Tipo.LITERAL);
    }),

    R10(),

    R11((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var id = (TokenLocalizado) ladoDireito.get(1);

        if (id.getTipo() != null) {
            output.append("scanf(\"");
            switch (id.getTipo()) {
                case LITERAL: output.append(" %[^\\n]s\", "); break;
                case INTEIRO: output.append("%d\", &"); break;
                case REAL: output.append("%f\", &"); break;
            }
            output.append(id.getLexema()).append(");\n");

            if (id.getTipo() == Tipo.LITERAL) {
                output.append("setbuf(stdin, NULL);\n");
            }
        }
        else {
            throw new ErroSemanticoException("Variável " + id.getLexema() + " não declarada.", id.getLinha(), id.getColuna());
        }
    }),

    R12((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var ARG = (NaoTerminalEAtributos) ladoDireito.get(1);

        switch (ARG.getTipo()) {
            case LITERAL: output.append("printf(\"%s\", ").append(ARG.getLexema()); break;
            case INTEIRO: output.append("printf(\"%d\", ").append(ARG.getLexema()); break;
            case REAL: output.append("printf(\"%f\", ").append(ARG.getLexema()); break;
        }

        output.append(");\n");
    }),

    R13((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        // ladoEsquerdo = ARG
        final var literal = (TokenLocalizado) ladoDireito.get(0);

        ladoEsquerdo.setAtributos(literal.getTokenEAtributos());
    }),

    R14((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        // ladoEsquerdo = ARG
        final var num = (TokenLocalizado) ladoDireito.get(0);

        ladoEsquerdo.setAtributos(num.getTokenEAtributos());
    }),

    R15((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        // ladoEsquerdo = ARG
        final var id = (TokenLocalizado) ladoDireito.get(0);

        ladoEsquerdo.setAtributos(id.getTokenEAtributos());
    }),

    R16(),

    R17((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var id = (TokenLocalizado) ladoDireito.get(0);
        final var LD = (NaoTerminalEAtributos) ladoDireito.get(2);

        if (id.getTipo() != null) {
            if (id.getTipo() == LD.getTipo()) {
                output.append(id.getLexema()).append(" = ").append(LD.getLexema()).append(";\n");
            }
            else {
                throw new ErroSemanticoException("Variável " + id.getLexema() + " é do tipo " + id.getTipo() + ", não " +LD.getTipo(), id.getLinha(), id.getColuna());
            }
        }
        else {
            throw new ErroSemanticoException("Variável " + id.getLexema() + " não declarada.", id.getLinha(), id.getColuna());
        }
    }),

    R18((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var OPRD1 = (NaoTerminalEAtributos) ladoDireito.get(0);
        final var opm = (TokenLocalizado) ladoDireito.get(1);
        final var OPRD2 = (NaoTerminalEAtributos) ladoDireito.get(2);

        if (OPRD1.getTipo() == OPRD2.getTipo() && OPRD1.getTipo() != Tipo.LITERAL) {
            var lexemaTemporario = "T" + listaVTemp.size();
            listaVTemp.add(OPRD1.getTipo() + " " + lexemaTemporario + ";\n");
            ladoEsquerdo.getAtributos().setLexema(lexemaTemporario);
            ladoEsquerdo.getAtributos().setTipo(OPRD1.getTipo());
            output.append(lexemaTemporario).append(" = ").append(OPRD1.getLexema()).append(" ").append(opm.getLexema()).append(" ").append(OPRD2.getLexema()).append(";\n");
        }
        else {
            throw new ErroSemanticoException("Operados com tipos incompatíveis " + OPRD1.getTipo() + " e " + OPRD2.getTipo(), opm.getLinha(), opm.getColuna());
        }
    }, true),

    R19((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var OPRD1 = (NaoTerminalEAtributos) ladoDireito.get(0);
        ladoEsquerdo.setAtributos(OPRD1.getAtributos());
    }),

    R20((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var id = (TokenLocalizado) ladoDireito.get(0);
        if (id.getTipo() != null){
            ladoEsquerdo.setAtributos(id.getTokenEAtributos());
        }
        else{
            throw new ErroSemanticoException("Variável " + id.getLexema() + " não declarada.", id.getLinha(), id.getColuna());
        }
    }),

    R21((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var num = (TokenLocalizado) ladoDireito.get(0);
        ladoEsquerdo.setAtributos(num.getTokenEAtributos());
    }),

    R22(),

    R23((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        output.append("}\n");
    }),

    R24((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var EXP_R = (NaoTerminalEAtributos) ladoDireito.get(2);
        output.append("if (").append(EXP_R.getLexema()).append(") {\n");
    }),

    R25((ladoEsquerdo, ladoDireito, listaVTemp, output) -> {
        final var OPRD1 = (NaoTerminalEAtributos) ladoDireito.get(0);
        final var opr = (TokenLocalizado) ladoDireito.get(1);
        final var OPRD2 = (NaoTerminalEAtributos) ladoDireito.get(2);

        if (OPRD1.getTipo() == OPRD2.getTipo()) {
            var oprEmC = opr.getLexema();
            switch (oprEmC) {
                case "=": oprEmC = "=="; break;
                case "<>": oprEmC = "!="; break;
            }
            var lexemaTemporario = "T" + listaVTemp.size();
            listaVTemp.add("int " + lexemaTemporario + ";\n");
            ladoEsquerdo.getAtributos().setLexema(lexemaTemporario);
            ladoEsquerdo.getAtributos().setTipo(Tipo.INTEIRO);
            output.append(lexemaTemporario).append(" = ").append(OPRD1.getLexema()).append(" ").append(oprEmC).append(" ").append(OPRD2.getLexema()).append(";\n");
        }
        else {
            throw new ErroSemanticoException("Operados com tipos incompatíveis " + OPRD1.getTipo() + " e " + OPRD2.getTipo(), opr.getLinha(), opr.getColuna());
        }
    }, true),

    R26(),

    R27(),

    R28(),

    R29(),

    R30();

    private interface Regra {
        void aplicar(final NaoTerminalEAtributos ladoEsquerdo,
                     final List<Object> ladoDireitoContextualizado,
                     final List<String> variaveisTemporarias,
                     final StringBuilder output) throws IOException, ErroSemanticoException;
    }

    private final Regra regra;
    final boolean criaVariavelTemp;

    RegraSemantica(final Regra regra, final boolean criaVariavelTemp) {
        this.regra = regra;
        this.criaVariavelTemp = criaVariavelTemp;
    }

    RegraSemantica(final Regra regra) {
        this(regra,false);
    }

    RegraSemantica() {
        this((ladoEsquerdo, ladoDireito, listaVTemp, output) -> { },false);
    }

    public void aplicar(final NaoTerminalEAtributos ladoEsquerdo,
                        final List<Object> ladoDireitoContextualizado,
                        final List<String> variaveisTemporarias,
                        final StringBuilder output) throws IOException, ErroSemanticoException {
        this.regra.aplicar(ladoEsquerdo, ladoDireitoContextualizado, variaveisTemporarias, output);
    }
}
