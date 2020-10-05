package partesCompilador;

import dominio.DFALexico;
import dominio.Erro;
import dominio.TokenEAtributos;
import dominio.enums.Token;
import dominio.excecoes.EstadoDeErroException;
import dominio.excecoes.FimDeTokenValidoException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalisadorLexico {

    // O código fonte representado como uma Lista de String (linhas e colunas)
    private final List<String> codigoFonte;
    // A tabela de símbolos representada como um Mapa (HashMap) que leva de lexema (String) à TokenEAtributos
    private final Map<String, TokenEAtributos> tabelaDeSimbolos = new HashMap<>();

    private final List<Erro> erros = new ArrayList<>();

    private final DFALexico DFA = new DFALexico();

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
        tabelaDeSimbolos.put(Token.lit.toString(), Token.lit.criaComAtributos());
        tabelaDeSimbolos.put(Token.inteiro.toString(), Token.inteiro.criaComAtributos());
        tabelaDeSimbolos.put(Token.real.toString(), Token.real.criaComAtributos());
    }

    public AnalisadorLexico(final List<String> codigoFonte) {
        this.codigoFonte = codigoFonte;
        iniciaTabelaDeSimbolos();
    }

    public TokenEAtributos lexico() {
        // Se acabaram as linhas, acabou o arquivo e retornamos EOF
        if (linha >= codigoFonte.size()) {
            return Token.EOF.criaComAtributos();
        }

        // Adicionamos o \n (quebra de linha) no fim de cada linha para garantir que, após ler o último caractere da linha,
        // o DFA tenha mais um caractere (que sempre é um finalizador de token) para ler.
        final String linhaAtual = codigoFonte.get(linha) + "\n";
        final StringBuilder lexema = new StringBuilder();
        DFA.resetar();

        try {
            while (true) {
                // Se acabaram as colunas, lemos da próxima linha
                if (coluna >= linhaAtual.length()) {
                    linha++;
                    coluna = 0;
                    return lexico();
                }
                final char caractereAtual = linhaAtual.charAt(coluna);
                coluna++;
                // Se estamos no S0, qualquer caracter em branco é considerado identação e ignorado
                if (DFA.getEstado() == DFALexico.ESTADO_INICIAL && Character.isWhitespace(caractereAtual)) {
                    continue;
                }
                DFA.aplicarFuncaoTrasicao(caractereAtual);
                lexema.append(caractereAtual);
            }

        } catch (final EstadoDeErroException e1) {
            coluna--;
            final Erro erro = new Erro(e1.getMessage(), linha+1, coluna+1);
            erros.add(erro);
            System.out.println(erro);

            return Token.ERRO.criaComAtributos(lexema.toString());

        } catch (final FimDeTokenValidoException e2) {
            coluna--;
            final Token token = DFA.getEstado().getTokenAssociado();
            TokenEAtributos tokenEAtributos = token.criaComAtributos(lexema.toString());

            if (token == Token.id) {
                final TokenEAtributos tokenJaNaTabela = tabelaDeSimbolos.get(lexema.toString());

                // Já está na tebela, então pegamos a versão na tabela
                if (tokenJaNaTabela != null) {
                    tokenEAtributos = tokenJaNaTabela;
                }
                // Não está na tebela, então colocamos
                else {
                    tabelaDeSimbolos.put(lexema.toString(), tokenEAtributos);
                }
            }
            System.out.println(tokenEAtributos);

            return tokenEAtributos;
        }
    }

    public Map<String, TokenEAtributos> getTabelaDeSimbolos() {
        return tabelaDeSimbolos;
    }

    public List<Erro> getErros() {
        return erros;
    }
}
