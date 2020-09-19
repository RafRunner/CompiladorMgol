package dominio;

import java.util.List;
import java.util.stream.Collectors;

public class CaracteresDeEscape {

    // Lista dos caracteres de escape padrões em C
    public static final List<Character> CARACTERES_VALIDOS = List.of('a', 'b', 'e', 'f', 'n', 'r', 't', 'v', '\\', '\'', '"', '?');

    // String com todas as sequências validas. É montada mapeando a lista acima com uma fução que adiciona o prefixo \ e juntando tudo
    // seprando por vírgula e espaço
    public static final String SEQUENCIAS_VALIDAS = CARACTERES_VALIDOS.stream().map(c -> "\\" + c).collect(Collectors.joining(", "));
}
