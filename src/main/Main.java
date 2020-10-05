package main;

import dominio.LeitorArquivos;
import dominio.TokenEAtributos;
import dominio.enums.Token;
import partesCompilador.AnalisadorLexico;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static List<TokenEAtributos> scanneaCodigoFonte(final List<String> codigoFonte) {
        final AnalisadorLexico analisadorLexico = new AnalisadorLexico(codigoFonte);

        final List<TokenEAtributos> tokens = new ArrayList<>();
        Token tokenAtual = null;

        while (!Token.EOF.equals(tokenAtual)) {
            final TokenEAtributos tokenEAtributos = analisadorLexico.lexico();
            tokens.add(tokenEAtributos);
            tokenAtual = tokenEAtributos.getToken();
        }

        return tokens;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Número incorreto de argumentos! Informe apenas o nome do arquivo!");
            return;
        }

        final String nomeArquivo = args[0];
        if (!nomeArquivo.endsWith(".mgol")) {
            System.out.println("Formato de aquivo não suportado! Deve ser do tipo .mgol");
            return;
        }

        List<String> codigoFonte;

        try {
            codigoFonte = LeitorArquivos.lerArquivo(nomeArquivo);
        } catch (FileNotFoundException exception) {
            System.out.println("Arquivo não encontrado!");
            return;
        }

        scanneaCodigoFonte(codigoFonte);

    }
}
