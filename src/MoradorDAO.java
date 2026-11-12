import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MoradorDAO {

    public void inserir(Morador m) throws SQLException {
        String sql = "INSERT INTO morador (nome, endereco, email, telefone) VALUES (?, ?, ?, ?)";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, m.getNome());
            ps.setString(2, m.getEndereco());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getTelefone());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                m.setId(rs.getInt(1));
            }
        } finally {
            con.close();
        }
    }

    public Morador buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM morador WHERE id_morador = ?";
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

    public List<Morador> listar() throws SQLException {
        List<Morador> lista = new ArrayList<>();
        String sql = "SELECT * FROM morador ORDER BY nome";
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

    public void atualizar(Morador m) throws SQLException {
        String sql = "UPDATE morador SET nome = ?, endereco = ?, email = ?, telefone = ? WHERE id_morador = ?";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, m.getNome());
            ps.setString(2, m.getEndereco());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getTelefone());
            ps.setInt(5, m.getId());
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM morador WHERE id_morador = ?";
        Connection con = Conexao.getConexao();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    private Morador montar(ResultSet rs) throws SQLException {
        return new Morador(rs.getInt("id_morador"), rs.getString("nome"), rs.getString("telefone"),
                rs.getString("endereco"), rs.getString("email"));
    }
}
