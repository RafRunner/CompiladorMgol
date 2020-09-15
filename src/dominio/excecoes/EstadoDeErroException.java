package dominio.excecoes;

public class EstadoDeErroException extends Exception {

    public EstadoDeErroException(final String mensagem) {
        super(mensagem);
    }
}
