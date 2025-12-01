package br.com.bitpay.dao;

import br.com.bitpay.model.Usuario;
import br.com.bitpay.model.Enums.TipoUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public int inserir(Connection conn, Usuario usuario) throws SQLException {

    	String sql = "INSERT INTO Usuarios (id, cpf, senha, email, idTipoUsuario) " +
                     "VALUES (seq_Usuarios.NEXTVAL, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {
            stmt.setString(1, usuario.getCpf());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getEmail());
            stmt.setInt(4, usuario.getTipoUsuario().getCodigo()); 

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir usuário, nenhuma linha afetada.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Falha ao obter ID gerado do usuário.");
                }
            }
        }
    }

    public boolean existeUsuario(Connection conn, String cpf, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE cpf = ? OR email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public Usuario buscarUsuarioBase(String email, String senha, Connection conn) throws SQLException {

        String sql = """
            SELECT 
                id,
                cpf,
                email,
                idTipoUsuario
            FROM Usuarios
            WHERE UPPER(email) = UPPER(?) AND TRIM(senha) = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                
                int idTipoUsuario = rs.getInt("idTipoUsuario");
                u.setTipoUsuario(TipoUsuario.getByCodigo(idTipoUsuario)); 
                return u;
            }
        }

        return null;
    }
}
