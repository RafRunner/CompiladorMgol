package dominio;

import dominio.enums.EstadoDFA;
import dominio.excecoes.EstadoDeErroException;

public class DFAMgol {

    private EstadoDFA estado = EstadoDFA.S0;

    public EstadoDFA aplicarFuncaoTrasicao(final char caractere) throws EstadoDeErroException {
        estado = estado.aplicarFuncaoTrasicao(caractere);
        return estado;
    }

    public void resetar() {
        estado = EstadoDFA.S0;
    }
}
