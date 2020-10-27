package partesCompilador.analisadorLexico.excecoes;

// Exceção lançada pela função de transição de um estado do DFA final quando o caractere lido não leva a nenhum outro estado
public class FimDeTokenValidoException extends Exception {

    public FimDeTokenValidoException(final String mensagem) {
        super(mensagem);
    }
}
