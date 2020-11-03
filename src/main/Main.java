package main;

import dominio.Erro;
import dominio.LeitorArquivos;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSintatico.AnalisadorSintatico;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    private final static List<String> formatosSuportados = List.of(".mgol", ".alg", ".mgl");
    private final static String stringFormatosSuportados = String.join(" ou ", formatosSuportados);

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

        final List<Erro> erros = new ArrayList<>();

        final AnalisadorLexico analisadorLexico = new AnalisadorLexico(codigoFonte, erros,false);
        final AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(analisadorLexico);
        analisadorSintatico.analisa();
    }
}
