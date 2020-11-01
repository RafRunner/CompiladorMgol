package dominio.enums;

import dominio.TokenEAtributos;

public enum Token {

    // Palavra reservada
    inicio,
    // Palavra reservada
    varinicio,
    // Palavra reservada
    varfim,
    // Ponto e vírgula
    PT_V,
    // Identificador
    id,
    // Palavra reservada
    inteiro,
    // Palavra reservada
    real,
    // Palavra reservada
    lit,
    // Palavra reservada
    leia,
    // Palavra reservada
    escreva,
    // Uma Sting
    Literal,
    // Um número (real ou inteiro)
    NUM,
    // Atribuição
    RCB,
    // Operadores aritméticos
    OPM,
    // Palavra reservada
    se,
    // Palavra reservada
    entao,
    // Operadores relacionais
    OPR,
    // Palavra reservada
    fimse,
    // Palavra reservada
    fim,
    // Abre parênteses
    AB_P,
    // Fecha parênteses
    FC_P,
    // Fim do arquivo
    EOF,

    // Ficam no final pois são ignorados pelo analisador sintático e não estão na tabela sintática
    Comentario,
    ERRO;

    public TokenEAtributos darAtributos(final String lexema, final Tipo tipo) {
        return new TokenEAtributos(this, lexema, tipo);
    }

    public TokenEAtributos darAtributos(final String lexema) {
        return new TokenEAtributos(this, lexema, null);
    }

    public TokenEAtributos darAtributos() {
        return new TokenEAtributos(this, this.toString(), null);
    }
}
