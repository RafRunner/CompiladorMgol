package main;

import dominio.Erro;
import dominio.LeitorArquivos;
import dominio.enums.Cor;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSintatico.AnalisadorSintatico;

import java.io.FileNotFoundException;
import java.util.*;


public class Main {

    private final static List<String> formatosSuportados = List.of("mgol", "alg", "mgl");
    private final static String stringFormatosSuportados = String.join(" ou ", formatosSuportados);
    private final static String regexFormatos = "\\.(" + String.join("|", formatosSuportados) + ")$";

    public static void main(String[] args) {
        if (Arrays.asList(args).contains("-h") || args.length == 0) {
            imprimeAjuda();
            return;
        }

        final String nomeArquivo = args[args.length - 1];

        // Se o arquivo não termina com nenhum dos finais suportados, é um erro
        if (formatosSuportados.stream().noneMatch(nomeArquivo::endsWith)) {
            Cor.imprimeComCor("Formato não suportado para o arquivo: " + nomeArquivo + "! Deve ser do tipo " + stringFormatosSuportados, Cor.RED);
            return;
        }

        int verbosidade = 0;
        String nomeArquivoSaida = nomeArquivo.replaceAll("\\G.+[\\\\\\/]", "").replaceAll(regexFormatos, ".c");

        for (int i = 0; i < args.length - 1; i++) {
            final String arg = args[i];

            if (arg.equals("-h")) {
                imprimeAjuda();
                return;
            }
            if (arg.equals("-v")) {
                i++;
                try {
                    verbosidade = Integer.parseInt(args[i]);
                } catch (NumberFormatException ignorede) {
                    Cor.imprimeComCor("Opção inválida para o argumento -v (verbosidade). Deve ser um número", Cor.RED);
                    return;
                }
                continue;
            }
            if (arg.equals("-o")) {
                i++;
                nomeArquivoSaida = args[i] + ".c";
            }
            else {
                Cor.imprimeComCor("Uso incorreto do compilador! Veja a ajuda abaixo:", Cor.RED);
                imprimeAjuda();
                return;
            }
        }

        final List<String> codigoFonte;

        try {
            codigoFonte = LeitorArquivos.lerArquivo(nomeArquivo);
        } catch (FileNotFoundException exception) {
            Cor.imprimeComCor("Arquivo \"" + nomeArquivo + "\" não encontrado!", Cor.RED);
            return;
        }

        final List<Erro> erros = new ArrayList<>();

        final AnalisadorLexico analisadorLexico = new AnalisadorLexico(codigoFonte, erros, verbosidade);
        final AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(analisadorLexico, verbosidade);
        analisadorSintatico.analisa();

        imprimeErrosOuSucesso(erros, nomeArquivoSaida);
    }

    private static void imprimeAjuda() {
        Cor.imprimeComCor("Esse é o compilador MGOL, capaz de compilar da linguagem fictícia MGOL para C.", Cor.BLUE);
        Cor.imprimeComCor("O último argumento do compilador deve sempre ser o caminho até o arquivo que contém o código fonte que se deseja compilar.", Cor.BLUE);
        Cor.imprimeComCor("Por padrão o compilador irá gerar o código objeto na mesma pasta onde está sendo executado com o mesmo nome do arquivo fonte.", Cor.BLUE);

        Cor.imprimeComCor("\nEle suporta alguns modificadores. Eles são:", Cor.WHITE);
        Cor.imprimeComCor("\t-h (ajuda): imprime essa mensagem", Cor.PURPLE);

        Cor.imprimeComCor("\n\t-v (verbosidade): argumento numérico para facilitar depuração de erros. Por padrão é zero. Opções:", Cor.PURPLE);
        Cor.imprimeComCor("\t\t-v 0 -> Não imprime nada durante a análise/compilação. Erros (se existirem) são listados ao final.", Cor.CYAN);
        Cor.imprimeComCor("\t\t-v 1 -> Erros e avisos são impressos durante a análise/compilação.", Cor.CYAN);
        Cor.imprimeComCor("\t\t-v 2 -> Tudo durante o processo de análise/compilação é impresso. Pode ficar confuso.", Cor.CYAN);

        Cor.imprimeComCor("\n\t-o (saída): argumento para definir o caminho e nome do arquivo de saída. Ex: -o ~/Documents/saida", Cor.PURPLE);

        Cor.imprimeComCor("\nFeito por: Rafael Nunes Santana e Armando Soares Neto.", Cor.GREEN);
    }

    private static void imprimeErrosOuSucesso(final List<Erro> erros, final String nomeArquivoSaida) {
        if (erros.size() != 0) {
            Cor.imprimeComCor("\nCompilação não concluída por erros. Resumo dos erros:", Cor.BLUE);
            Cor.imprimeComCor("------------------------------------------------------------------------------------------------------", Cor.BLUE);
            for (final Erro r : erros) {
                Cor.imprimeComCor(r.toString(), Cor.RED);
            }
        }
        else {
            Cor.imprimeComCor("\nCompilação concluída com sucesso. Arquivo de saída: " + nomeArquivoSaida, Cor.BLUE);
        }
    }
}
