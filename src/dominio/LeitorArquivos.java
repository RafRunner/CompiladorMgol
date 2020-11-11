package dominio;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LeitorArquivos {

    // Produção significa que o programa está compilado e rodando no jar
    public static boolean producao;

    static {
        try {
            producao = new File(LeitorArquivos.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath().contains(".jar");
            // Nunca deve acontecer pois estamos gerando o URI da própria location, mas o Java é paranoico
        } catch (final URISyntaxException ignored) {
            producao = false;
        }
    }

    // Função que lê um arquivo pelo camnho e devolve uma lista de Strings (as linhas)
    public static List<String> lerArquivo(final String fileName) throws FileNotFoundException {
        final File file = new File(fileName);
        return lerArquivo(new Scanner(file));
    }

    private static List<String> lerArquivo(final Scanner scanner) {
        final List<String> linhas = new ArrayList<>();

        while (scanner.hasNextLine()) {
            final String linha = scanner.nextLine();
            linhas.add(linha);
        }

        scanner.close();
        return linhas;
    }

    public static List<String> lerResource(final String path) throws FileNotFoundException {
        if (!producao) {
            // Quando não está em produção, está na pasta res/, no raiz pro projeto. Em produção está na pasta src (raiz do jar)
            return lerArquivo("res" + path);
        }

        final InputStream is = LeitorArquivos.class.getResourceAsStream(path);
        return lerArquivo(new Scanner(is));
    }

    // Função para ler um arquivo .csv da pasta res ou do jar. Retorna uma List com as linhas (lista de células)
    public static List<List<String>> lerCsvResource(final String fileName) throws FileNotFoundException {
        final List<String> linhas = lerResource(fileName);
        return linhas.stream().map(linha -> List.of(linha.split(",", -1))).collect(Collectors.toList());
    }
}
