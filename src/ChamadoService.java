import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ChamadoService {
    private ChamadoDAO chamadoDAO = new ChamadoDAO();
    private MoradorDAO moradorDAO = new MoradorDAO();
    private AgenteDAO agenteDAO = new AgenteDAO();

    public Chamado abrirChamado(int idMorador, int tipo, String descricao, String localizacao,
                                String detalhe) throws RegraDeNegocioException, SQLException {
        Morador morador = moradorDAO.buscarPorId(idMorador);
        if (morador == null) {
            throw new RegraDeNegocioException("Morador nao encontrado.");
        }
        if (descricao.equals("") || localizacao.equals("")) {
            throw new RegraDeNegocioException("Descricao e localizacao sao obrigatorias.");
        }

        LocalDate agora = LocalDate.now();
        Chamado chamado;
        if (tipo == 1) {
            chamado = new ChamadoInfraestrutura(0, descricao, localizacao, agora, StatusChamado.ABERTO, detalhe);
        } else if (tipo == 2) {
            chamado = new ChamadoIluminacao(0, descricao, localizacao, agora, StatusChamado.ABERTO, detalhe);
        } else if (tipo == 3) {
            chamado = new ChamadoLimpeza(0, descricao, localizacao, agora, StatusChamado.ABERTO, detalhe);
        } else {
            throw new RegraDeNegocioException("Tipo de chamado invalido.");
        }

        morador.abrirChamado(chamado);
        chamadoDAO.inserir(chamado);
        return chamado;
    }

    public List<Chamado> listarPorMorador(int idMorador) throws SQLException {
        return chamadoDAO.listarPorMorador(idMorador);
    }

    public List<Chamado> listarPorStatus(String status) throws SQLException {
        return chamadoDAO.listarPorStatus(status);
    }

    public Chamado avancarStatus(int idChamado, int idAgente) throws RegraDeNegocioException, SQLException {
        Chamado chamado = chamadoDAO.buscarPorId(idChamado);
        if (chamado == null) {
            throw new RegraDeNegocioException("Chamado nao encontrado.");
        }
        AgenteMunicipal agente = agenteDAO.buscarPorId(idAgente);
        if (agente == null) {
            throw new RegraDeNegocioException("Agente nao encontrado.");
        }

        String anterior = chamado.getStatus();
        agente.atualizarStatus(chamado);
        chamadoDAO.atualizarStatus(chamado, anterior);
        return chamado;
    }

    public void editarChamado(int idChamado, int idMorador, String descricao,
                              String localizacao) throws RegraDeNegocioException, SQLException {
        Chamado chamado = chamadoDAO.buscarPorId(idChamado);
        if (chamado == null) {
            throw new RegraDeNegocioException("Chamado nao encontrado.");
        }
        if (chamado.getIdMorador() != idMorador) {
            throw new RegraDeNegocioException("So o morador que abriu pode editar o chamado.");
        }
        if (!descricao.equals("")) {
            chamado.setDescricao(descricao);
        }
        if (!localizacao.equals("")) {
            chamado.setLocalizacao(localizacao);
        }
        chamadoDAO.atualizarTexto(chamado);
    }

    public void cancelarChamado(int idChamado, int idMorador) throws RegraDeNegocioException, SQLException {
        Chamado chamado = chamadoDAO.buscarPorId(idChamado);
        if (chamado == null) {
            throw new RegraDeNegocioException("Chamado nao encontrado.");
        }
        if (chamado.getIdMorador() != idMorador) {
            throw new RegraDeNegocioException("So o morador que abriu pode cancelar o chamado.");
        }
        chamadoDAO.excluir(idChamado);
    }

    public List<String> historico(int idChamado) throws SQLException {
        return chamadoDAO.listarHistorico(idChamado);
    }
}
