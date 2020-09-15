package dominio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LeitorArquivos {

    // Função que lê um arquivo pelo camnho e devolve uma lista de Strings (as linhas)
    public static List<String> lerArquivo(final String fileName) throws FileNotFoundException {
        final List<String> linhas = new ArrayList<>();
        final File file = new File(fileName);

        try (final Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                final String linha = scanner.nextLine();
                linhas.add(linha);
            }
        }

        return linhas;
    }
}
