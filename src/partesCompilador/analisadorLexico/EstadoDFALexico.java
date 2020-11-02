package partesCompilador.analisadorLexico;

import dominio.CaracteresDeEscape;
import dominio.enums.Token;
import partesCompilador.analisadorLexico.excecoes.EstadoDeErroException;
import partesCompilador.analisadorLexico.excecoes.FimDeTokenValidoException;
import partesCompilador.analisadorLexico.interfaces.FuncaoTransicao;

// Enum dos possiveis estados do DFA, se o token associado é null, não é estado final, caso contrário é
public enum EstadoDFALexico {

    S0(null, caractere -> {
        if (Character.isAlphabetic(caractere)) {
            return values()[8];
        }
        if (Character.isDigit(caractere)) {
            return values()[1];
        }
        switch (caractere) {
            case '"': return values()[6];
            case '{': return values()[9];
            case '=': return values()[11];
            case '>': return values()[12];
            case '<': return values()[13];
            case '/':
            case '*':
            case '-':
            case '+': return values()[14];
            case '(': return values()[16];
            case ')': return values()[17];
            case ';': return values()[18];
        }

        // Nesse caso a análise deve continuar do caractere após o caractere inesperado (não deve ser lido novamente)
        throw new EstadoDeErroException("Caractere inesperado: " + caractere, ((coluna, linhaAtual) -> coluna));
    }),

    S1(Token.num, caractere -> {
        if (Character.isDigit(caractere)) {
            return values()[1];
        }
        switch (caractere) {
            case '.': return values()[2];
            case 'e':
            case 'E': return values()[4];
        }

        throw new FimDeTokenValidoException("Fim de declaração de constante numérica");
    }),

    S2(null, caractere -> {
        if (Character.isDigit(caractere)) {
            return values()[3];
        }

        throw new EstadoDeErroException("Erro ao definir constante numérica. Esperado: dígito, lido: " + caractere);
    }),

    S3(Token.num, caractere -> {
        if (Character.isDigit(caractere)) {
            return values()[3];
        }
        if (caractere == 'e' || caractere == 'E') {
            return values()[4];
        }

        throw new FimDeTokenValidoException("Fim de declaração de constante numérica");
    }),

    S4(null, caractere -> {
        if (Character.isDigit(caractere)) {
            return values()[20];
        }
        if (caractere == '+' || caractere == '-') {
            return values()[5];
        }

        throw new EstadoDeErroException("Erro ao definir constante numérica em notação científica. Esperado: dígito, + ou -, lido: " + caractere);
    }),

    S5(null, caractere -> {
        if (Character.isDigit(caractere)) {
            return values()[20];
        }

        throw new EstadoDeErroException("Erro ao definir constante numérica em notação científica. Dígito esperado, lido: " + caractere);
    }),

    S6(null, caractere -> {
        switch (caractere) {
            case '"' : return values()[7];
            case '\\': return values()[19];
            case '\n': throw new EstadoDeErroException("Quebra de linha inesperada no meio de declaração de constante literal");
        }

        return values()[6];
    }),

    S7(Token.literal, caractere -> {
        throw new FimDeTokenValidoException("Fim de declaração de constante literal");
    }),

    S8(Token.id, caractere -> {
        if (Character.isAlphabetic(caractere) || Character.isDigit(caractere) || caractere == '_') {
            return values()[8];
        }

        throw new FimDeTokenValidoException("Fim de declaração de identificador");
    }),

    S9(null, caractere -> {
        if (caractere == '}') {
            return values()[10];
        }

        return values()[9];
    }),

    S10(Token.comentario, caractere -> {
        throw new FimDeTokenValidoException("Fim de comentário");
    }),

    S11(Token.opr, caractere -> {
        throw new FimDeTokenValidoException("Fim de operador relacional");
    }),

    S12(Token.opr, caractere -> {
        if (caractere == '=') {
            return values()[11];
        }

        throw new FimDeTokenValidoException("Fim de operador relacional");
    }),

    S13(Token.opr, caractere -> {
        switch (caractere) {
            case '=':
            case '>': return values()[11];
            case '-': return values()[15];
        }

        throw new FimDeTokenValidoException("Fim de operador relacional");
    }),

    S14(Token.opm, caractere -> {
        throw new FimDeTokenValidoException("Fim de operador aritmético");
    }),

    S15(Token.rcb, caractere -> {
        throw new FimDeTokenValidoException("Fim de operador de atribuição");
    }),

    S16(Token.ab_p, caractere -> {
        throw new FimDeTokenValidoException("Fim de abre parênteses");
    }),

    S17(Token.fc_p, caractere -> {
        throw new FimDeTokenValidoException("Fim de fecha parênteses");
    }),

    S18(Token.pt_v, caractere -> {
        throw new FimDeTokenValidoException("Fim de ponto e vírgula");
    }),

    S19(null, caractere -> {
        if (CaracteresDeEscape.CARACTERES_VALIDOS.contains(caractere)) {
            return values()[6];
        }

        // Nesse caso a análise deve continuar a partir do caractere após o fim da string (depois do segundo ") ou do final da linha se a string não for fechada
        throw new EstadoDeErroException("Sequência de escape: \\" + caractere + " inválida. Sequências válidas: " + CaracteresDeEscape.SEQUENCIAS_VALIDAS,
                ((coluna, linhaAtual) -> {
                    final int indiceFimString = linhaAtual.indexOf("\"", linhaAtual.indexOf("\"") + 1);
                    return indiceFimString == -1 ? linhaAtual.length() - 1 : indiceFimString + 1;
                }));
    }),

    S20(Token.num, caractere -> {
        if (Character.isDigit(caractere)) {
            return values()[20];
        }

        throw new FimDeTokenValidoException("Fim de declaração de constante numérica");
    });

    private final Token tokenAssociado;
    private final FuncaoTransicao funcaoTransicao;

    EstadoDFALexico(final Token tokenAssociado, final FuncaoTransicao funcaoTransicao) {
        this.tokenAssociado = tokenAssociado;
        this.funcaoTransicao = funcaoTransicao;
    }

    public Token getTokenAssociado() {
        return tokenAssociado;
    }

    public EstadoDFALexico aplicarFuncaoTrasicao(final char caractere) throws EstadoDeErroException, FimDeTokenValidoException {
        return funcaoTransicao.aplicar(caractere);
    }
}
