package main;

import dominio.Erro;
import dominio.LeitorArquivos;
import dominio.enums.Cor;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSintatico.AnalisadorSintatico;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    private final static List<String> formatosSuportados = List.of(".mgol", ".alg", ".mgl");
    private final static String stringFormatosSuportados = String.join(" ou ", formatosSuportados);

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 2) {
            System.out.println("Número incorreto de argumentos! Informe o nome do arquivo ou a verbosidade (ex: -v2) e o nome do arquivo.");
            return;
        }

        final String nomeArquivo;
        final int verbosidade;

        if (args.length == 2) {
            nomeArquivo = args[1];
            verbosidade = Integer.parseInt(args[0].substring(2));
        }
        else {
            nomeArquivo = args[0];
            verbosidade = 0;
        }

        // Se o arquivo não termina com nenhum dos finais suportados, é um erro
        if (formatosSuportados.stream().noneMatch(nomeArquivo::endsWith)) {
            System.out.println("Formato de aquivo não suportado! Deve ser do tipo " + stringFormatosSuportados);
            return;
        }

        final List<String> codigoFonte;

        try {
            codigoFonte = LeitorArquivos.lerArquivo(nomeArquivo);
        } catch (FileNotFoundException exception) {
            System.out.println("Arquivo não encontrado!");
            return;
        }

        final List<Erro> erros = new ArrayList<>();

        final AnalisadorLexico analisadorLexico = new AnalisadorLexico(codigoFonte, erros, verbosidade);
        final AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(analisadorLexico, verbosidade);
        analisadorSintatico.analisa();

        if (erros.size() != 0) {
            Cor.imprimeComCor("Resumo dos erros:", Cor.BLUE);
            Cor.imprimeComCor("---------------------------------------------------------------------------------------", Cor.BLUE);
            for (final Erro r : erros) {
                Cor.imprimeComCor(r.toString(), Cor.RED);
            }
        }
    }
}
