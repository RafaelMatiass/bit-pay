package br.com.bitpay.dao;

import br.com.bitpay.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public int inserir(Connection conn, Usuario usuario) throws SQLException {
        // Inserção usando sequence e RETURNING para obter ID gerado
        String sql = "INSERT INTO Usuarios (id, cpf, senha, email, idTipoUsuario) " +
                     "VALUES (seq_Usuarios.NEXTVAL, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {
            stmt.setString(1, usuario.getCPF());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getEmail());
            stmt.setInt(4, usuario.getTipoUsuario().getCodigo()); // Certifique-se que existe no banco

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir usuário, nenhuma linha afetada.");
            }

            // Captura o ID gerado
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
}
