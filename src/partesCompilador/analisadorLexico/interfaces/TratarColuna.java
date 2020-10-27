package partesCompilador.analisadorLexico.interfaces;

// interface que define a função usada para dizer de onde a analise de uma coluna deve continuar após um erro. Veja @EstadoDFALexico
public interface TratarColuna {
    int aplicar(final int coluna, final String linhaAtual);
}
