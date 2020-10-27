package main;

import dominio.LeitorArquivos;
import dominio.TokenEAtributos;
import dominio.TokenLocalizado;
import dominio.enums.Token;
import partesCompilador.AnalisadorLexico;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    private final static List<String> formatosSuportados = List.of(".mgol", ".alg", ".mgl");
    private final static String stringFormatosSuportados = String.join(" ou ", formatosSuportados);

    private static List<TokenEAtributos> scanneaCodigoFonte(final AnalisadorLexico analisadorLexico) {
        final List<TokenEAtributos> tokens = new ArrayList<>();
        Token tokenAtual = null;

        while (!Token.EOF.equals(tokenAtual)) {
            final TokenLocalizado tokenEAtributos = analisadorLexico.lexico();
            tokens.add(tokenEAtributos.getTokenEAtributos());
            tokenAtual = tokenEAtributos.getToken();
        }

        return tokens;
    }

    public static List<TokenEAtributos> scanneaCodigoFonte(final List<String> codigoFonte) {
        return scanneaCodigoFonte(new AnalisadorLexico(codigoFonte));
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Número incorreto de argumentos! Informe o nome do arquivo e somente ele!");
            return;
        }

        final String nomeArquivo = args[0];
        // Se o arquivo não termina com nenhum dos finais suportados, é um erro
        if (formatosSuportados.stream().noneMatch(nomeArquivo::endsWith)) {
            System.out.println("Formato de aquivo não suportado! Deve ser do tipo " + stringFormatosSuportados);
            return;
        }

        List<String> codigoFonte;

        try {
            codigoFonte = LeitorArquivos.lerArquivo(nomeArquivo);
        } catch (FileNotFoundException exception) {
            System.out.println("Arquivo não encontrado!");
            return;
        }

        final AnalisadorLexico analisadorLexico = new AnalisadorLexico(codigoFonte);
        scanneaCodigoFonte(analisadorLexico);
        System.out.println("\n" + analisadorLexico.tabelaDeSimbolosToString());
    }
}
