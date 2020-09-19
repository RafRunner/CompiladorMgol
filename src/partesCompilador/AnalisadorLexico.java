package partesCompilador;

import dominio.TokenEAtributos;
import dominio.enums.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalisadorLexico {

    // O código fonte representado como uma Lista de String (linhas e colunas)
    private final List<String> codigoFonte;
    // A tabela de símbolos representada como um Mapa (HashMap) que leva de lexema (String) à TokenEAtributos
    private final Map<String, TokenEAtributos> tabelaDeSimbolos = new HashMap<>();

    private int linha = 0;
    private int coluna = 0;

    // Populando a tabela de símbolos com as palavras reservadas
    private void iniciaTabelaDeSimbolos() {
        tabelaDeSimbolos.put(Token.inicio.toString(), Token.inicio.criaComAtributos());
        tabelaDeSimbolos.put(Token.varinicio.toString(), Token.varinicio.criaComAtributos());
        tabelaDeSimbolos.put(Token.varfim.toString(), Token.varfim.criaComAtributos());
        tabelaDeSimbolos.put(Token.escreva.toString(), Token.escreva.criaComAtributos());
        tabelaDeSimbolos.put(Token.leia.toString(), Token.leia.criaComAtributos());
        tabelaDeSimbolos.put(Token.se.toString(), Token.se.criaComAtributos());
        tabelaDeSimbolos.put(Token.entao.toString(), Token.entao.criaComAtributos());
        tabelaDeSimbolos.put(Token.fimse.toString(), Token.fimse.criaComAtributos());
        tabelaDeSimbolos.put(Token.fim.toString(), Token.fim.criaComAtributos());
    }

    public AnalisadorLexico(final List<String> codigoFonte) {
        this.codigoFonte = codigoFonte;
        iniciaTabelaDeSimbolos();
    }

    public TokenEAtributos lexico() {
        return null;
    }

    public Map<String, TokenEAtributos> getTabelaDeSimbolos() {
        return tabelaDeSimbolos;
    }
}
