import java.time.LocalDate;

public abstract class Chamado {
    private int id;
    private int idMorador;
    private int idAgente;
    private String descricao;
    private String localizacao;
    private LocalDate dataAbertura;
    private String status;

    public Chamado(int id, String descricao, String localizacao,
                   LocalDate dataAbertura, String status) {
        this.id = id;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.dataAbertura = dataAbertura;
        this.status = status;
    }

    public abstract int calcularPrioridade();
    public abstract String getTipo();
    public abstract String getDetalhe();

    public String getPrioridadeTexto() {
        int p = this.calcularPrioridade();
        if (p == 3) {
            return "ALTA";
        }
        if (p == 2) {
            return "MEDIA";
        }
        return "BAIXA";
    }

    public String getResumo() {
        return "Chamado #" + this.id + " [" + this.getTipo() + "] " + this.descricao
                + " | local: " + this.localizacao
                + " | status: " + this.status
                + " | prioridade: " + this.getPrioridadeTexto();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMorador() { return idMorador; }
    public void setIdMorador(int idMorador) { this.idMorador = idMorador; }
    public int getIdAgente() { return idAgente; }
    public void setIdAgente(int idAgente) { this.idAgente = idAgente; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
