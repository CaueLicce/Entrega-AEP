public class AgenteMunicipal extends Pessoa {
    private String matricula;
    private String setor;

    public AgenteMunicipal(int id, String nome, String telefone, String matricula, String setor) {
        super(id, nome, telefone);
        this.matricula = matricula;
        this.setor = setor;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public void atualizarStatus(Chamado chamado) throws RegraDeNegocioException {
        String novoStatus = StatusChamado.proximo(chamado.getStatus());
        chamado.setIdAgente(this.getId());
        chamado.setStatus(novoStatus);
    }

    @Override
    public String getDados() {
        return "#" + getId() + " | " + getNome() + " | tel: " + getTelefone()
                + " | matricula: " + this.matricula + " | setor: " + this.setor;
    }
}
