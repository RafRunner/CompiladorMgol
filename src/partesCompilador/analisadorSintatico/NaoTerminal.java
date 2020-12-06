package partesCompilador.analisadorSintatico;

import dominio.NaoTerminalEAtributos;
import dominio.TokenEAtributos;
import dominio.enums.Token;

import java.util.Set;

// Símbolos não terminais da gramática
public enum NaoTerminal {

    P_LINHA(Set.of(Token.eof)),
    P(Set.of(Token.eof)),
    V(Set.of(Token.leia, Token.escreva, Token.id, Token.se, Token.fim)),
    LV(Set.of(Token.leia, Token.escreva, Token.id, Token.se, Token.fim)),
    D(Set.of(Token.id, Token.varfim)),
    TIPO(Set.of(Token.pt_v)),
    A(Set.of(Token.eof)),
    ES(Set.of(Token.leia, Token.escreva, Token.id, Token.se, Token.fim, Token.fimse)),
    ARG(Set.of(Token.pt_v)),
    CMD(Set.of(Token.leia, Token.escreva, Token.id, Token.se, Token.fim, Token.fimse)),
    LD(Set.of(Token.pt_v)),
    OPRD(Set.of(Token.opm, Token.pt_v, Token.opr, Token.fc_p)),
    COND(Set.of(Token.leia, Token.escreva, Token.id, Token.se, Token.fim, Token.fimse)),
    CABECALHO(Set.of(Token.leia, Token.escreva, Token.id, Token.se, Token.fimse)),
    EXP_R(Set.of(Token.ab_p)),
    CORPO(Set.of(Token.leia, Token.escreva, Token.id, Token.se, Token.fim, Token.fimse));

    // É o conjunto follow
    private final Set<Token> tokensSincronizacao;

    NaoTerminal(final Set<Token> tokensSincronizacao) {
        this.tokensSincronizacao = tokensSincronizacao;
    }

    public Set<Token> getTokensSincronizacao() {
        return tokensSincronizacao;
    }

    public NaoTerminalEAtributos darAtributos() {
        return new NaoTerminalEAtributos(this, new TokenEAtributos());
    }
}
