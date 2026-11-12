import java.time.LocalDate;

public class ChamadoIluminacao extends Chamado {
    public static final String ESCOLA = "Escola";
    public static final String HOSPITAL = "Hospital";
    public static final String PRACA = "Praca";
    public static final String OUTRO = "Outro";
    public static final String[] OPCOES = {ESCOLA, HOSPITAL, PRACA, OUTRO};

    private String pontoReferencia;

    public ChamadoIluminacao(int id, String descricao, String localizacao,
                             LocalDate dataAbertura, String status, String pontoReferencia) {
        super(id, descricao, localizacao, dataAbertura, status);
        this.pontoReferencia = pontoReferencia;
    }

    @Override
    public int calcularPrioridade() {
        if (this.pontoReferencia.equals(ESCOLA) || this.pontoReferencia.equals(HOSPITAL)) {
            return 3;
        }
        if (this.pontoReferencia.equals(PRACA)) {
            return 2;
        }
        return 1;
    }

    @Override
    public String getTipo() { return "ILUMINACAO"; }

    @Override
    public String getDetalhe() { return this.pontoReferencia; }
}
