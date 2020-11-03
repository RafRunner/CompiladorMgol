package partesCompilador.analisadorLexico;

import dominio.Erro;
import dominio.TokenEAtributos;
import dominio.TokenLocalizado;
import dominio.enums.Token;
import partesCompilador.analisadorLexico.excecoes.EstadoDeErroException;
import partesCompilador.analisadorLexico.excecoes.FimDeTokenValidoException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalisadorLexico {

    // O código fonte representado como uma Lista de String (linhas e colunas)
    private final List<String> codigoFonte;
    // A tabela de símbolos representada como um Mapa (HashMap) que leva de lexema (String) à TokenEAtributos
    private final Map<String, TokenEAtributos> tabelaDeSimbolos = new HashMap<>();

    private final List<Erro> erros;

    private final DFALexico DFA = new DFALexico();

    private int linha = 0;
    private int coluna = 0;

    private final boolean verboso;

    // Populando a tabela de símbolos com as palavras reservadas
    private void iniciaTabelaDeSimbolos() {
        tabelaDeSimbolos.put(Token.inicio.toString(), Token.inicio.darAtributos());
        tabelaDeSimbolos.put(Token.varinicio.toString(), Token.varinicio.darAtributos());
        tabelaDeSimbolos.put(Token.varfim.toString(), Token.varfim.darAtributos());
        tabelaDeSimbolos.put(Token.escreva.toString(), Token.escreva.darAtributos());
        tabelaDeSimbolos.put(Token.leia.toString(), Token.leia.darAtributos());
        tabelaDeSimbolos.put(Token.se.toString(), Token.se.darAtributos());
        tabelaDeSimbolos.put(Token.entao.toString(), Token.entao.darAtributos());
        tabelaDeSimbolos.put(Token.fimse.toString(), Token.fimse.darAtributos());
        tabelaDeSimbolos.put(Token.fim.toString(), Token.fim.darAtributos());
        tabelaDeSimbolos.put(Token.lit.toString(), Token.lit.darAtributos());
        tabelaDeSimbolos.put(Token.inteiro.toString(), Token.inteiro.darAtributos());
        tabelaDeSimbolos.put(Token.real.toString(), Token.real.darAtributos());
    }

    public AnalisadorLexico(final List<String> codigoFonte, List<Erro> erros, final boolean verboso) {
        this.codigoFonte = codigoFonte;
        this.verboso = verboso;
        this.erros = erros;
        iniciaTabelaDeSimbolos();
    }

    public AnalisadorLexico(final List<String> codigoFonte, final List<Erro> erros) {
        this(codigoFonte, erros,true);
    }
    
    public TokenLocalizado lerProximoTokenNaoComentario() {
        TokenLocalizado tokenLocalizado = lerProximoToken();
        
        while (tokenLocalizado.getToken() == Token.comentario) {
            tokenLocalizado = lerProximoToken();
        }
        
        return tokenLocalizado;
    }

    public TokenLocalizado lerProximoToken() {
        // Se acabaram as linhas, acabou o arquivo e retornamos EOF
        if (linha >= codigoFonte.size()) {
            final TokenEAtributos eof = Token.eof.darAtributos("");
            if (verboso) {
                System.out.println(eof);
            }
            return eof.localizar(linha, coluna);
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
                    return lerProximoToken();
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
            final Erro erro = new Erro("Erro léxico: " + e1.getMessage(), linha + 1, coluna);
            System.out.println("\n" + erro + "\n");
            erros.add(erro);

            lexema.append(linhaAtual.charAt(coluna - 1));
            TokenLocalizado tokenErro = Token.erro.darAtributos(lexema.toString()).localizar(linha, coluna);

            coluna = e1.aplicarTratarColuna(coluna, linhaAtual);

            return tokenErro;

        } catch (final FimDeTokenValidoException e2) {
            coluna--;
            final Token token = DFA.getEstado().getTokenAssociado();
            TokenEAtributos tokenEAtributos = token.darAtributos(lexema.toString());

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
            if (verboso) {
                System.out.println(tokenEAtributos);
            }

            return tokenEAtributos.localizar(linha, coluna);
        }
    }

    public Map<String, TokenEAtributos> getTabelaDeSimbolos() {
        return tabelaDeSimbolos;
    }

    public List<Erro> getErros() {
        return erros;
    }

    public String tabelaDeSimbolosToString() {
        final StringBuilder sb = new StringBuilder("Tabela de símbolos:\n");

        tabelaDeSimbolos.forEach((key, value) -> sb.append(key).append(" -> ").append(value).append("\n"));

        return sb.toString();
    }
}
