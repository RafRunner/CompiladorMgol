package partesCompilador.analisadorSintatico;

import dominio.enums.Token;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum RegraGramatical {

    r01(NaoTerminal.P_LINHA,   List.of(NaoTerminal.P)),
    r02(NaoTerminal.P,         List.of(Token.inicio, NaoTerminal.V, NaoTerminal.A)),
    r03(NaoTerminal.V,         List.of(Token.varinicio, NaoTerminal.LV)),
    r04(NaoTerminal.LV,        List.of(NaoTerminal.D, NaoTerminal.LV)),
    r05(NaoTerminal.LV,        List.of(Token.varfim, Token.pt_v)),
    r06(NaoTerminal.D,         List.of(Token.id, NaoTerminal.TIPO, Token.pt_v)),
    r07(NaoTerminal.TIPO,      List.of(Token.inteiro)),
    r08(NaoTerminal.TIPO,      List.of(Token.real)),
    r09(NaoTerminal.TIPO,      List.of(Token.lit)),
    r10(NaoTerminal.A,         List.of(NaoTerminal.ES, NaoTerminal.A)),
    r11(NaoTerminal.ES,        List.of(Token.leia, Token.id, Token.pt_v)),
    r12(NaoTerminal.ES,        List.of(Token.escreva, NaoTerminal.ARG, Token.pt_v)),
    r13(NaoTerminal.ARG,       List.of(Token.literal)),
    r14(NaoTerminal.ARG,       List.of(Token.num)),
    r15(NaoTerminal.ARG,       List.of(Token.id)),
    r16(NaoTerminal.A,         List.of(NaoTerminal.CMD, NaoTerminal.A)),
    r17(NaoTerminal.CMD,       List.of(Token.id, Token.rcb, NaoTerminal.LD, Token.pt_v)),
    r18(NaoTerminal.LD,        List.of(NaoTerminal.OPRD, Token.opm, NaoTerminal.OPRD)),
    r19(NaoTerminal.LD,        List.of(NaoTerminal.OPRD)),
    r20(NaoTerminal.OPRD,      List.of(Token.id)),
    r21(NaoTerminal.OPRD,      List.of(Token.num)),
    r22(NaoTerminal.A,         List.of(NaoTerminal.COND, NaoTerminal.A)),
    r23(NaoTerminal.COND,      List.of(NaoTerminal.CABECALHO, NaoTerminal.CORPO)),
    r24(NaoTerminal.CABECALHO, List.of(Token.se, Token.ab_p, NaoTerminal.EXP_R, Token.fc_p, Token.entao)),
    r25(NaoTerminal.EXP_R,     List.of(NaoTerminal.OPRD, Token.opr, NaoTerminal.OPRD)),
    r26(NaoTerminal.CORPO,     List.of(NaoTerminal.ES, NaoTerminal.CORPO)),
    r27(NaoTerminal.CORPO,     List.of(NaoTerminal.CMD, NaoTerminal.CORPO)),
    r28(NaoTerminal.CORPO,     List.of(NaoTerminal.COND, NaoTerminal.CORPO)),
    r29(NaoTerminal.CORPO,     List.of(Token.fimse)),
    r30(NaoTerminal.A,         List.of(Token.fim));

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
