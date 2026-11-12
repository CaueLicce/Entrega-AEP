import java.time.LocalDate;

public class ChamadoLimpeza extends Chamado {
    public static final String PEQUENO = "Pequeno";
    public static final String MEDIO = "Medio";
    public static final String GRANDE = "Grande";
    public static final String[] OPCOES = {PEQUENO, MEDIO, GRANDE};

    private String volumeEstimado;

    public ChamadoLimpeza(int id, String descricao, String localizacao,
                          LocalDate dataAbertura, String status, String volumeEstimado) {
        super(id, descricao, localizacao, dataAbertura, status);
        this.volumeEstimado = volumeEstimado;
    }

    @Override
    public int calcularPrioridade() {
        if (this.volumeEstimado.equals(GRANDE)) {
            return 3;
        }
        if (this.volumeEstimado.equals(MEDIO)) {
            return 2;
        }
        return 1;
    }

    @Override
    public String getTipo() { return "LIMPEZA"; }

    @Override
    public String getDetalhe() { return this.volumeEstimado; }
}
