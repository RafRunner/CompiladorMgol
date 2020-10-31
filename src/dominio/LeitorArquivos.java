package dominio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LeitorArquivos {

    // Função que lê um arquivo pelo camnho e devolve uma lista de Strings (as linhas)
    public static List<String> lerArquivo(final String fileName) throws FileNotFoundException {
        final File file = new File(fileName);
        final List<String> linhas = new ArrayList<>();

        try (final Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                final String linha = scanner.nextLine();
                linhas.add(linha);
            }
        }

        return linhas;
    }

    // Função para ler um arquivo .csv da pasta res. Retorna uma List com as linhas (lista de células)
    public static List<List<String>> lerCsv(final String fileName) throws FileNotFoundException {
        final List<String> linhas = lerArquivo(fileName);
        return linhas.stream().map(linha -> List.of(linha.split(","))).collect(Collectors.toList());
    }
}
