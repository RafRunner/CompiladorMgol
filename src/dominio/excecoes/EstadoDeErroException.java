package dominio.excecoes;

// Exceção lançada pela função de transição de um estado do DFA não final quando o caractere lido não leva a nenhum outro estado
public class EstadoDeErroException extends Exception {

    public EstadoDeErroException(final String mensagem) {
        super(mensagem);
    }
}
