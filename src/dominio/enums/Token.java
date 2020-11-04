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
    pt_v,
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
    literal,
    // Um número (real ou inteiro)
    num,
    // Atribuição
    rcb,
    // Operadores aritméticos
    opm,
    // Palavra reservada
    se,
    // Palavra reservada
    entao,
    // Operadores relacionais
    opr,
    // Palavra reservada
    fimse,
    // Palavra reservada
    fim,
    // Abre parênteses
    ab_p,
    // Fecha parênteses
    fc_p,
    // Fim do arquivo
    eof,
    // Erro
    erro,

    // Fica no final pois comentários são ignorados pelo analisador sintático e não está na tabela sintática
    comentario;

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
