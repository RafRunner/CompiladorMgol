package partesCompilador.analisadorSemantico;


public class ErroSemanticoException extends Exception {

    private final Object mensagem;
    private final int linha;
    private final int coluna;

    public ErroSemanticoException(Object mensagem, int linha, int coluna) {
        this.mensagem = mensagem;
        this.linha = linha;
        this.coluna = coluna;
    }

    public Object getMensagem() {
        return mensagem;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}
