package main;

import dominio.Erro;
import dominio.LeitorArquivos;
import dominio.enums.Cor;
import main.arguments.Argument;
import main.arguments.ArgumentParser;
import main.arguments.ArgumentType;
import main.arguments.InvalidArgumentException;
import partesCompilador.analisadorLexico.AnalisadorLexico;
import partesCompilador.analisadorSemantico.AnalisadorSemantico;
import partesCompilador.analisadorSintatico.AnalisadorSintatico;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Main {

    private final static List<String> formatosSuportados = List.of("mgol", "alg", "mgl");
    private final static String stringFormatosSuportados = String.join(" ou ", formatosSuportados);
    private final static String regexFormatos = "\\.(" + String.join("|", formatosSuportados) + ")$";

    public static void main(String[] args) throws IOException {
        if (args.length == 0 || args[0].equals("-h")) {
            imprimeAjuda(true);
            return;
        }

        final String nomeArquivo = args[args.length - 1];

        // Se o arquivo não termina com nenhum dos finais suportados, é um erro
        if (formatosSuportados.stream().noneMatch(nomeArquivo::endsWith)) {
            Cor.imprimeComCor("Formato não suportado para o arquivo: " + nomeArquivo + "! Deve ser do tipo " + stringFormatosSuportados, Cor.RED);
            return;
        }

        final String nomeSaidaDefault = nomeArquivo.replaceAll("\\G.+[\\\\\\/]", "").replaceAll(regexFormatos, "");

        final ArgumentParser argumentParser;
        try {
            argumentParser = new ArgumentParser(
                    List.of(new Argument(ArgumentType.INTEGER, "verbosidade", 'v', "0"),
                            new Argument(ArgumentType.STRING, "nome arquivo saída", 'o', nomeSaidaDefault),
                            new Argument(ArgumentType.FLAG, "help", 'h', "false"))
                    , Arrays.copyOfRange(args, 0, args.length - 1));
        } catch (InvalidArgumentException ignored) {
            Cor.imprimeComCor("Argumentos inválidos! Veja ajuda abaixo:\n", Cor.RED);
            imprimeAjuda(false);
            return;
        }

        final int verbosidade;
        final String nomeArquivoSaida;
        try {
            if (argumentParser.getBollArgument('h')) {
                imprimeAjuda(false);
                return;
            }

            verbosidade = argumentParser.getIntArgument('v');
            nomeArquivoSaida = argumentParser.getStringArgument('o') + ".c";
        } catch (InvalidArgumentException e) {
            Cor.imprimeComCor(e.getMessage(), Cor.RED);
            return;
        }

        final List<String> codigoFonte;

        try {
            codigoFonte = LeitorArquivos.lerArquivo(nomeArquivo);
        } catch (FileNotFoundException exception) {
            Cor.imprimeComCor("Arquivo \"" + nomeArquivo + "\" não encontrado!", Cor.RED);
            return;
        }

        final long inicio = System.currentTimeMillis();

        final List<Erro> erros = new ArrayList<>();

        final AnalisadorLexico analisadorLexico = new AnalisadorLexico(codigoFonte, erros, verbosidade);
        final AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico(erros, verbosidade);
        final AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(analisadorLexico, analisadorSemantico, verbosidade);

        analisadorSintatico.analisa();

        if (erros.size() != 0) {
            Cor.imprimeComCor("\nCompilação não concluída por erros. Resumo dos erros:", Cor.BLUE);
            Cor.imprimeComCor("---------------------------------------------------------------------------------------------------------", Cor.BLUE);
            for (final Erro r : erros) {
                Cor.imprimeComCor(r.toString(), Cor.RED);
            }
            System.out.println();
        }
        else {
            final BufferedWriter output = new BufferedWriter(new FileWriter(nomeArquivoSaida));
            analisadorSemantico.escreveArquivo(output);
            final long tempo = System.currentTimeMillis() - inicio;

            Cor.imprimeComCor("\nCompilação concluída com sucesso em " + tempo + "ms. Arquivo de saída: " + nomeArquivoSaida + "\n", Cor.BLUE);
        }
    }

    private static void imprimeAjuda(final boolean imprimeheader) {
        if (imprimeheader) {

            Cor.imprimeComCor("                                              ___                  __\n" +
                    "                                       __    /\\_ \\                /\\ \\\n" +
                    "  ___     ___     ___ ___     _____   /\\_\\   \\//\\ \\       __      \\_\\ \\     ___    _ __\n" +
                    " /'___\\  / __`\\ /' __` __`\\  /\\ '__`\\ \\/\\ \\    \\ \\ \\    /'__`\\    /'_` \\   / __`\\ /\\`'__\\\n" +
                    "/\\ \\__/ /\\ \\_\\ \\/\\ \\/\\ \\/\\ \\ \\ \\ \\_\\ \\ \\ \\ \\    \\_\\ \\_ /\\ \\_\\.\\_ /\\ \\_\\ \\ /\\ \\_\\ \\\\ \\ \\/\n" +
                    "\\ \\____\\\\ \\____/\\ \\_\\ \\_\\ \\_\\ \\ \\ ,__/  \\ \\_\\   /\\____\\\\ \\__/.\\_\\\\ \\_____\\\\ \\____/ \\ \\_\\\n" +
                    " \\/____/ \\/___/  \\/_/\\/_/\\/_/  \\ \\ \\/    \\/_/   \\/____/ \\/__/\\/_/ \\/____ / \\/___/   \\/_/\n" +
                    "                                \\ \\_\\\n" +
                    "                                 \\/_/", Cor.YELLOW);

            Cor.imprimeComCor("          _____                    _____                   _______                   _____\n" +
                    "         /\\    \\                  /\\    \\                 /::\\    \\                 /\\    \\\n" +
                    "        /::\\____\\                /::\\    \\               /::::\\    \\               /::\\____\\\n" +
                    "       /::::|   |               /::::\\    \\             /::::::\\    \\             /:::/    /\n" +
                    "      /:::::|   |              /::::::\\    \\           /::::::::\\    \\           /:::/    /\n" +
                    "     /::::::|   |             /:::/\\:::\\    \\         /:::/~~\\:::\\    \\         /:::/    /\n" +
                    "    /:::/|::|   |            /:::/  \\:::\\    \\       /:::/    \\:::\\    \\       /:::/    /\n" +
                    "   /:::/ |::|   |           /:::/    \\:::\\    \\     /:::/    / \\:::\\    \\     /:::/    /\n" +
                    "  /:::/  |::|___|______    /:::/    / \\:::\\    \\   /:::/____/   \\:::\\____\\   /:::/    /\n" +
                    " /:::/   |::::::::\\    \\  /:::/    /   \\:::\\ ___\\ |:::|    |     |:::|    | /:::/    /\n" +
                    "/:::/    |:::::::::\\____\\/:::/____/  ___\\:::|    ||:::|____|     |:::|----|/:::/____/\n" +
                    "\\::/    / ~~~~~/:::/    /\\:::\\    \\ /\\  /:::|____| \\:::\\    \\   /:::/    / \\:::\\    \\\n" +
                    " \\/____/      /:::/    /  \\:::\\    /::\\ \\::/    /   \\:::\\    \\ /:::/    /   \\:::\\    \\\n" +
                    "             /:::/    /    \\:::\\   \\:::\\ \\/____/     \\:::\\    /:::/    /     \\:::\\    \\\n" +
                    "            /:::/    /      \\:::\\   \\:::\\____\\        \\:::\\__/:::/    /       \\:::\\    \\\n" +
                    "           /:::/    /        \\:::\\  /:::/    /         \\::::::::/    /         \\:::\\    \\\n" +
                    "          /:::/    /          \\:::\\/:::/    /           \\::::::/    /           \\:::\\    \\\n" +
                    "         /:::/    /            \\::::::/    /             \\::::/    /             \\:::\\    \\\n" +
                    "        /:::/    /              \\::::/    /               \\::/____/               \\:::\\____\\\n" +
                    "        \\::/    /                \\::/____/                                         \\::/    /\n" +
                    "         \\/____/                                                                    \\/____/\n" +
                    "\n", Cor.YELLOW);
        }

        Cor.imprimeComCor("\nUso: CompiladorMgol [modificadores] <arquivo fonte>", Cor.WHITE);

        Cor.imprimeComCor("\nEsse é o compilador MGOL, capaz de compilar da linguagem fictícia MGOL para C.", Cor.BLUE);
        Cor.imprimeComCor("O último argumento do compilador deve sempre ser o caminho até o arquivo que contém o código fonte que se deseja compilar.", Cor.BLUE);
        Cor.imprimeComCor("Por padrão o compilador irá gerar o código objeto na mesma pasta onde está sendo executado com o mesmo nome do arquivo fonte.", Cor.BLUE);
        Cor.imprimeComCor("Caso erros forem dectados durante a compilação, o arquivo objeto não será gerado e no lugar os erros serão listados", Cor.BLUE);

        Cor.imprimeComCor("\nEle suporta alguns modificadores. Eles são:", Cor.WHITE);
        Cor.imprimeComCor("\t-h (ajuda): imprime essa mensagem.", Cor.PURPLE);

        Cor.imprimeComCor("\n\t-v (verbosidade): argumento numérico para facilitar depuração de erros. Por padrão é zero. Opções:", Cor.PURPLE);
        Cor.imprimeComCor("\t\t-v 0 -> Não imprime nada durante a análise/compilação.", Cor.CYAN);
        Cor.imprimeComCor("\t\t-v 1 -> Erros e avisos são impressos durante a análise/compilação.", Cor.CYAN);
        Cor.imprimeComCor("\t\t-v 2 -> Tudo durante o processo de análise/compilação é impresso. Pode ficar confuso.", Cor.CYAN);

        Cor.imprimeComCor("\n\t-o (saída): argumento para definir o caminho e nome do arquivo de saída. Ex: -o ~/Documents/saida", Cor.PURPLE);

        Cor.imprimeComCor("\nFeito por: Rafael Nunes Santana e Armando Soares e Silva Neto.\n", Cor.GREEN);
    }
}
