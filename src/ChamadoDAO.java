import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChamadoDAO {

    public void inserir(Chamado c) throws SQLException {
        String sql = "INSERT INTO chamado (id_morador, tipo, descricao, localizacao, detalhe, data_abertura, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, c.getIdMorador());
            ps.setString(2, c.getTipo());
            ps.setString(3, c.getDescricao());
            ps.setString(4, c.getLocalizacao());
            ps.setString(5, c.getDetalhe());
            ps.setDate(6, Date.valueOf(c.getDataAbertura()));
            ps.setString(7, c.getStatus());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }
        } finally {
            con.close();
        }
        registrarHistorico(c.getId(), "", c.getStatus());
    }

    public Chamado buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM chamado WHERE id_chamado = ?";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return montar(rs);
            }
            return null;
        } finally {
            con.close();
        }
    }

    public List<Chamado> listarPorMorador(int idMorador) throws SQLException {
        List<Chamado> lista = new ArrayList<>();
        String sql = "SELECT * FROM chamado WHERE id_morador = ? ORDER BY data_abertura";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idMorador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } finally {
            con.close();
        }
        return lista;
    }

    public List<Chamado> listarPorStatus(String status) throws SQLException {
        List<Chamado> lista = new ArrayList<>();
        String sql = "SELECT * FROM chamado WHERE status = ? ORDER BY data_abertura";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } finally {
            con.close();
        }
        return lista;
    }

    public void atualizarStatus(Chamado c, String statusAnterior) throws SQLException {
        String sql = "UPDATE chamado SET status = ?, id_agente = ? WHERE id_chamado = ?";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getStatus());
            if (c.getIdAgente() == 0) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, c.getIdAgente());
            }
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        } finally {
            con.close();
        }
        registrarHistorico(c.getId(), statusAnterior, c.getStatus());
    }

    public void atualizarTexto(Chamado c) throws SQLException {
        String sql = "UPDATE chamado SET descricao = ?, localizacao = ? WHERE id_chamado = ?";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getDescricao());
            ps.setString(2, c.getLocalizacao());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM chamado WHERE id_chamado = ?";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    private void registrarHistorico(int idChamado, String anterior, String novo) throws SQLException {
        String sql = "INSERT INTO status_historico (id_chamado, status_anterior, status_novo, data_alteracao) "
                + "VALUES (?, ?, ?, ?)";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idChamado);
            if (anterior.equals("")) {
                ps.setNull(2, Types.VARCHAR);
            } else {
                ps.setString(2, anterior);
            }
            ps.setString(3, novo);
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    public List<String> listarHistorico(int idChamado) throws SQLException {
        List<String> linhas = new ArrayList<>();
        String sql = "SELECT * FROM status_historico WHERE id_chamado = ? ORDER BY id_historico";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idChamado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String anterior = rs.getString("status_anterior");
                if (anterior == null) {
                    anterior = "(criado)";
                }
                linhas.add(rs.getDate("data_alteracao") + " | " + anterior + " -> " + rs.getString("status_novo"));
            }
        } finally {
            con.close();
        }
        return linhas;
    }

    private Chamado montar(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_chamado");
        String descricao = rs.getString("descricao");
        String local = rs.getString("localizacao");
        LocalDate data = rs.getDate("data_abertura").toLocalDate();
        String status = rs.getString("status");
        String detalhe = rs.getString("detalhe");
        String tipo = rs.getString("tipo");

        Chamado c;
        switch (tipo) {
            case "INFRAESTRUTURA" -> c = new ChamadoInfraestrutura(id, descricao, local, data, status, detalhe);
            case "ILUMINACAO" -> c = new ChamadoIluminacao(id, descricao, local, data, status, detalhe);
            default -> c = new ChamadoLimpeza(id, descricao, local, data, status, detalhe);
        }
        c.setIdMorador(rs.getInt("id_morador"));
        c.setIdAgente(rs.getInt("id_agente"));
        return c;
    }
}
