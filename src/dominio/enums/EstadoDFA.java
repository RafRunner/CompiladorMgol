package dominio.enums;


import dominio.excecoes.EstadoDeErroException;

// Enum dos possiveis estados do DFA, se o token associado é null, não é estado final, caso contrário é
public enum EstadoDFA {

    S0(null, (caractere -> {
        if (Character.isAlphabetic(caractere)) {
            return EstadoDFA.S8;
        }
        if (Character.isDigit(caractere)) {
            return EstadoDFA.S1;
        }

        throw new EstadoDeErroException("Caractere inesperado: " + caractere);
    })),

    S1(Token.NUM),
    S2(null),
    S3(Token.NUM),
    S4(null),
    S5(null),
    S6(null),
    S7(Token.Literal),
    S8(Token.id),
    S9(null),
    S10(Token.Comentario),
    S11(Token.OPR),
    S12(Token.OPR),
    S13(Token.OPR),
    S14(Token.OPM),
    S15(Token.RCB),
    S16(Token.AB_P),
    S17(Token.FC_P),
    S18(Token.PT_V);

    // Função que leva de um estado a outro através de um caractere. Caso leia um caracter que não leva a outro estado,
    // lança a EstadoDeErroException com detalhes do erro
    private interface FuncaoTransicao {
        EstadoDFA aplicar(final char caractere) throws EstadoDeErroException;
    }

    private Token tokenAssociado;
    private FuncaoTransicao funcaoTransicao;

    EstadoDFA(final Token tokenAssociado, final FuncaoTransicao funcaoTransicao) {
        this.tokenAssociado = tokenAssociado;
        this.funcaoTransicao = funcaoTransicao;
    }

    public Token getTokenAssociado() {
        return tokenAssociado;
    }

    public EstadoDFA aplicarFuncaoTrasicao(final char caractere) throws EstadoDeErroException {
        return funcaoTransicao.aplicar(caractere);
    }
}
