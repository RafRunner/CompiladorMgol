package partesCompilador.analisadorSintatico.tabela;

import dominio.TokenEAtributos;
import dominio.TokenLocalizado;
import dominio.enums.Token;

public enum TipoErro {

    // Espaços vazios são E0
    E0(null, token -> "Token do tipo \"" + token.getToken() + "\" inesperado: " + token.getLexema()),

    // Casos onde assumimos estar faltando um token
    E1(Token.pt_v.darAtributos(";"), token -> "Faltando ponto e vírgula após \"" + token.getLexema() + "\""),
    E2(Token.fc_p.darAtributos(")"), token -> "Expressão de comparação não completada. Faltando um \")\" após \"" + token.getLexema() + "\"?"),
    E3(Token.ab_p.darAtributos("("), token -> "Faltando um \"(\" após \"se\""),
    E4(Token.varfim.darAtributos(), token -> "Faltando \"varfim\" após \"" + token.getLexema() + "\""),
    E5(Token.varinicio.darAtributos(), token -> "Faltando \"varinicio\" após \"inicio\""),
    E6(Token.inicio.darAtributos(), token -> "Faltando um \"inicio\" no topo do programa"),
    E7(Token.entao.darAtributos(), token -> "Faltando um \"entao\" para completar a expressão se"),

    // Outros casos
    E9(null, token -> "Token de id \"" + token.getLexema() + "\" solto (deveria ter atribuição)"),
    E10(null, token -> "Argumento inaceitável para leia/escreva: \"" + token.getToken() + "\"");

    private interface Detalhe {
        String montaDetalhe(final TokenLocalizado token);
    }

    private final TokenEAtributos tokenFaltando;
    private final Detalhe detalhe;

    TipoErro(final TokenEAtributos tokenFaltando, final Detalhe detalhe) {
        this.tokenFaltando = tokenFaltando;
        this.detalhe = detalhe;
    }

    public TokenEAtributos getTokenFaltando() {
        return tokenFaltando;
    }

    public String getDetalhe(final TokenLocalizado token) {
        return detalhe.montaDetalhe(token);
    }
}
