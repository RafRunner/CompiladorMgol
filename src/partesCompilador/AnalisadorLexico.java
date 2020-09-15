package partesCompilador;

import dominio.LeitorArquivos;
import dominio.TokenEAtributos;
import dominio.enums.Token;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalisadorLexico {

    private final List<String> codigoFonte;
    private final Map<String, TokenEAtributos> tabelaDeSimbolos = new HashMap<>();

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

    public AnalisadorLexico(final String nomeArquivoCodigoFonte) throws FileNotFoundException {
        this(LeitorArquivos.lerArquivo(nomeArquivoCodigoFonte));
    }

    public AnalisadorLexico(final List<String> codigoFonte) {
        this.codigoFonte = codigoFonte;
        iniciaTabelaDeSimbolos();
    }

    public TokenEAtributos lexico(final int linha, final int coluna) {
        return null;
    }

    public Map<String, TokenEAtributos> getTabelaDeSimbolos() {
        return tabelaDeSimbolos;
    }
}
