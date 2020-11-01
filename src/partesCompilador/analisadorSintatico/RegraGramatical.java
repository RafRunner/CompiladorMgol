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
    r05(NaoTerminal.LV,        List.of(Token.varfim, Token.PT_V)),
    r06(NaoTerminal.D,         List.of(Token.id, NaoTerminal.TIPO, Token.PT_V)),
    r07(NaoTerminal.TIPO,      List.of(Token.inteiro)),
    r08(NaoTerminal.TIPO,      List.of(Token.real)),
    r09(NaoTerminal.TIPO,      List.of(Token.lit)),
    r10(NaoTerminal.A,         List.of(NaoTerminal.ES, NaoTerminal.A)),
    r11(NaoTerminal.ES,        List.of(Token.leia, Token.id, Token.PT_V)),
    r12(NaoTerminal.ES,        List.of(Token.escreva, NaoTerminal.ARG, Token.PT_V)),
    r13(NaoTerminal.ARG,       List.of(Token.Literal)),
    r14(NaoTerminal.ARG,       List.of(Token.NUM)),
    r15(NaoTerminal.ARG,       List.of(Token.id)),
    r16(NaoTerminal.A,         List.of(NaoTerminal.CMD, NaoTerminal.A)),
    r17(NaoTerminal.CMD,       List.of(Token.id, Token.RCB, NaoTerminal.LD, Token.PT_V)),
    r18(NaoTerminal.LD,        List.of(NaoTerminal.OPRD, Token.OPM, NaoTerminal.OPRD)),
    r19(NaoTerminal.LD,        List.of(NaoTerminal.OPRD)),
    r20(NaoTerminal.OPRD,      List.of(Token.id)),
    r21(NaoTerminal.OPRD,      List.of(Token.NUM)),
    r22(NaoTerminal.A,         List.of(NaoTerminal.COND, NaoTerminal.A)),
    r23(NaoTerminal.COND,      List.of(NaoTerminal.CABECALHO, NaoTerminal.CORPO)),
    r24(NaoTerminal.CABECALHO, List.of(Token.se, Token.AB_P, NaoTerminal.EXP_R, Token.FC_P, Token.entao)),
    r25(NaoTerminal.EXP_R,     List.of(NaoTerminal.OPRD, Token.OPR, NaoTerminal.OPRD)),
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
        return ladoEsquedo.toString() + "->" + ladoDireito.stream().map(Objects::toString).collect(Collectors.joining(" "));
    }
}
