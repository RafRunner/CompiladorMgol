package dominio.excecoes;

import dominio.interfaces.TratarColuna;

// Exceção lançada pela função de transição de um estado do DFA não final quando o caractere lido não leva a nenhum outro estado
public class EstadoDeErroException extends Exception {

    private final TratarColuna tratarColuna;

    public EstadoDeErroException(final String mensagem) {
        super(mensagem);
        tratarColuna = ((coluna, linhaAtual) -> coluna - 1 );
    }

    public EstadoDeErroException(final String mensagem, final TratarColuna tratarColuna){
        super(mensagem);
        this.tratarColuna = tratarColuna;
    }

    public int aplicarTratarColuna(final int coluna, final String linhaAtual){
        return tratarColuna.aplicar(coluna, linhaAtual);
    }
}
