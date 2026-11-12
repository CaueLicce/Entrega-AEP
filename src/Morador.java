import java.util.ArrayList;
import java.util.List;

public class Morador extends Pessoa {
    private String endereco;
    private String email;
    private List<Chamado> chamados = new ArrayList<>();

    public Morador(int id, String nome, String telefone, String endereco, String email) {
        super(id, nome, telefone);
        this.endereco = endereco;
        this.email = email;
    }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Chamado abrirChamado(Chamado chamado) {
        chamado.setIdMorador(this.getId());
        this.chamados.add(chamado);
        return chamado;
    }

    public List<Chamado> listarChamados() {
        return this.chamados;
    }

    @Override
    public String getDados() {
        return "#" + getId() + " | " + getNome() + " | tel: " + getTelefone()
                + " | " + this.email + " | " + this.endereco;
    }
}
