package dominio.enums;

import dominio.TokenEAtributos;

public enum Token {

    NUM,
    Literal,
    id,
    Comentario,

    // Fim do arquivo
    EOF,

    // Operadores relacionais
    OPR,

    // Atribuição
    RCB,

    // Operadores aritméticos
    OPM,

    // Abre parênteses
    AB_P,

    // Fecha parênteses
    FC_P,

    // Ponto e vírgula
    PT_V,

    ERRO,

    /* PALAVRAS RESERVADAS */

    inicio,
    varinicio,
    varfim,
    escreva,
    leia,
    se,
    entao,
    fimse,
    fim;

    public TokenEAtributos criaComAtributos(final String lexema, final Tipo tipo) {
        return new TokenEAtributos(this, lexema, tipo);
    }

    public TokenEAtributos criaComAtributos(final String lexema) {
        return new TokenEAtributos(this, lexema, null);
    }

    public TokenEAtributos criaComAtributos() {
        return new TokenEAtributos(this, this.toString(), null);
    }
}
