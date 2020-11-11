package partesCompilador.analisadorSintatico.tabela;

import dominio.TokenEAtributos;
import dominio.TokenLocalizado;
import dominio.enums.Token;

public enum TipoErro {

    // Essas são o tratamento de erro genérico (modo pânico)
    // Espaços vazios são E0
    E0(true,null, token -> "Token do tipo \"" + token.getToken() + "\" inesperado: " + token.getLexema()),
    E11(false,null, token -> "Declaração da variável \"" + token.getLexema() + "\" deve ser feita na seção de variáveis"),
    E15(true,null, token -> "Espaço de variáveis usado para comando: \"" + token.getLexema() + "\""),
    E16(false,null, token -> "Atribuição da variável \"" + token.getLexema() + "\" deve ser feito fora do espaço de variáveis"),

    // Casos onde assumimos estar faltando um token
    E1(false, Token.pt_v.darAtributos(";"), token -> "Faltando ponto e vírgula após \"" + token.getLexema() + "\""),
    E2(false, Token.fc_p.darAtributos(")"), token -> "Expressão de comparação não completada. Faltando um \")\" após \"" + token.getLexema() + "\"?"),
    E3(false, Token.ab_p.darAtributos("("), token -> "Faltando um \"(\" após \"se\""),
    E4(false, Token.varfim.darAtributos(), token -> "Faltando \"varfim\" após \"" + token.getLexema() + "\""),
    E5(false, Token.varinicio.darAtributos(), token -> "Faltando \"varinicio\" após \"inicio\""),
    E6(false, Token.inicio.darAtributos(), token -> "Faltando um \"inicio\" no topo do programa"),
    E7(false, Token.entao.darAtributos(), token -> "Faltando um \"entao\" para completar a expressão se"),
    E8(false, Token.fim.darAtributos(), token -> "Faltando um \"fim\" no final do programa"),
    E12(false, Token.fimse.darAtributos(), token -> "Algum \"se\" está sem \"fimse\""),
    E14(true, Token.eof.darAtributos(""), token -> "Token inesperado \"" + token.getLexema() + "\" após \"fim\". A partir daqui tudo será ignorado."),

    // Outros casos
    E9(false,null, token -> "Variável \"" + token.getLexema() + "\" sem atribuição"),
    E10(true,null, token -> "Argumento inaceitável para leia/escreva: \"" + token.getToken() + "\""),
    E13(false, null, token -> "Fatando operador %s após \"" + token.getLexema() + "\"");

    private interface Detalhe {
        String montaDetalhe(final TokenLocalizado token);
    }

    private final boolean usaAtual;
    private final TokenEAtributos tokenFaltando;
    private final Detalhe detalhe;

    TipoErro(final boolean usaAtual, final TokenEAtributos tokenFaltando, final Detalhe detalhe) {
        this.usaAtual = usaAtual;
        this.tokenFaltando = tokenFaltando;
        this.detalhe = detalhe;
    }

    public boolean usaAtual() {
        return usaAtual;
    }

    public TokenEAtributos getTokenFaltando() {
        return tokenFaltando;
    }

    public String getDetalhe(final TokenLocalizado token) {
        return detalhe.montaDetalhe(token);
    }
}
