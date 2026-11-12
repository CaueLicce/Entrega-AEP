import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AgenteDAO {

    public void inserir(AgenteMunicipal a) throws SQLException {
        String sql = "INSERT INTO agente_municipal (nome, telefone, matricula, setor) VALUES (?, ?, ?, ?)";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, a.getNome());
            ps.setString(2, a.getTelefone());
            ps.setString(3, a.getMatricula());
            ps.setString(4, a.getSetor());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                a.setId(rs.getInt(1));
            }
        } finally {
            con.close();
        }
    }

    public AgenteMunicipal buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM agente_municipal WHERE id_agente = ?";
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

    public List<AgenteMunicipal> listar() throws SQLException {
        List<AgenteMunicipal> lista = new ArrayList<>();
        String sql = "SELECT * FROM agente_municipal ORDER BY nome";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } finally {
            con.close();
        }
        return lista;
    }

    private AgenteMunicipal montar(ResultSet rs) throws SQLException {
        return new AgenteMunicipal(rs.getInt("id_agente"), rs.getString("nome"), rs.getString("telefone"),
                rs.getString("matricula"), rs.getString("setor"));
    }
}
