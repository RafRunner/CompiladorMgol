package partesCompilador.analisadorSintatico;

import dominio.enums.Token;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum RegraGramatical {

    R1 (NaoTerminal.P_LINHA,   List.of(NaoTerminal.P)),
    R2 (NaoTerminal.P,         List.of(Token.inicio, NaoTerminal.V, NaoTerminal.A)),
    R3 (NaoTerminal.V,         List.of(Token.varinicio, NaoTerminal.LV)),
    R4 (NaoTerminal.LV,        List.of(NaoTerminal.D, NaoTerminal.LV)),
    R5 (NaoTerminal.LV,        List.of(Token.varfim, Token.pt_v)),
    R6 (NaoTerminal.D,         List.of(Token.id, NaoTerminal.TIPO, Token.pt_v)),
    R7 (NaoTerminal.TIPO,      List.of(Token.inteiro)),
    R8 (NaoTerminal.TIPO,      List.of(Token.real)),
    R9 (NaoTerminal.TIPO,      List.of(Token.lit)),
    R10(NaoTerminal.A,         List.of(NaoTerminal.ES, NaoTerminal.A)),
    R11(NaoTerminal.ES,        List.of(Token.leia, Token.id, Token.pt_v)),
    R12(NaoTerminal.ES,        List.of(Token.escreva, NaoTerminal.ARG, Token.pt_v)),
    R13(NaoTerminal.ARG,       List.of(Token.literal)),
    R14(NaoTerminal.ARG,       List.of(Token.num)),
    R15(NaoTerminal.ARG,       List.of(Token.id)),
    R16(NaoTerminal.A,         List.of(NaoTerminal.CMD, NaoTerminal.A)),
    R17(NaoTerminal.CMD,       List.of(Token.id, Token.rcb, NaoTerminal.LD, Token.pt_v)),
    R18(NaoTerminal.LD,        List.of(NaoTerminal.OPRD, Token.opm, NaoTerminal.OPRD)),
    R19(NaoTerminal.LD,        List.of(NaoTerminal.OPRD)),
    R20(NaoTerminal.OPRD,      List.of(Token.id)),
    R21(NaoTerminal.OPRD,      List.of(Token.num)),
    R22(NaoTerminal.A,         List.of(NaoTerminal.COND, NaoTerminal.A)),
    R23(NaoTerminal.COND,      List.of(NaoTerminal.CABECALHO, NaoTerminal.CORPO)),
    R24(NaoTerminal.CABECALHO, List.of(Token.se, Token.ab_p, NaoTerminal.EXP_R, Token.fc_p, Token.entao)),
    R25(NaoTerminal.EXP_R,     List.of(NaoTerminal.OPRD, Token.opr, NaoTerminal.OPRD)),
    R26(NaoTerminal.CORPO,     List.of(NaoTerminal.ES, NaoTerminal.CORPO)),
    R27(NaoTerminal.CORPO,     List.of(NaoTerminal.CMD, NaoTerminal.CORPO)),
    R28(NaoTerminal.CORPO,     List.of(NaoTerminal.COND, NaoTerminal.CORPO)),
    R29(NaoTerminal.CORPO,     List.of(Token.fimse)),
    R30(NaoTerminal.A,         List.of(Token.fim)),
    R31(NaoTerminal.LD,        List.of(Token.literal));

    private final NaoTerminal ladoEsquedo;
    private final List<Object> ladoDireito;

    RegraGramatical(final NaoTerminal ladoEsquedo, final List<Object> ladoDireito) {
        this.ladoEsquedo = ladoEsquedo;
        this.ladoDireito = ladoDireito;
    }

    public NaoTerminal getLadoEsquedo() {
        return ladoEsquedo;
    }

    public List<Object> getLadoDireito() {
        return ladoDireito;
    }

    @Override
    public String toString() {
        final int ordinal = ordinal() + 1;
        return (ordinal > 9 ? ordinal : "0" + ordinal)  + ") " + ladoEsquedo.toString() + "->"
                + ladoDireito.stream().map(Objects::toString).collect(Collectors.joining(" "));
    }
}
