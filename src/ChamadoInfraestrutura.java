import java.time.LocalDate;

public class ChamadoInfraestrutura extends Chamado {
    public static final String AVENIDA = "Avenida";
    public static final String RUA = "Rua";
    public static final String CALCADA = "Calcada";
    public static final String[] OPCOES = {AVENIDA, RUA, CALCADA};

    private String tipoVia;

    public ChamadoInfraestrutura(int id, String descricao, String localizacao,
                                 LocalDate dataAbertura, String status, String tipoVia) {
        super(id, descricao, localizacao, dataAbertura, status);
        this.tipoVia = tipoVia;
    }

    @Override
    public int calcularPrioridade() {
        if (this.tipoVia.equals(AVENIDA)) {
            return 3;
        }
        if (this.tipoVia.equals(RUA)) {
            return 2;
        }
        return 1;
    }

    @Override
    public String getTipo() { return "INFRAESTRUTURA"; }

    @Override
    public String getDetalhe() { return this.tipoVia; }
}
