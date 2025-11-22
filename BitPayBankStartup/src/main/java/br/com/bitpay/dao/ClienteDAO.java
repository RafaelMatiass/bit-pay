package br.com.bitpay.dao;

import br.com.bitpay.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    public int inserir(Connection conn, Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Clientes (id, nome, dataNascimento, dataCadastro, idUsuario, idEndereco, idTelefone) " +
                     "VALUES (seq_Clientes.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {
            stmt.setString(1, cliente.getNome());
             // dataNascimento (pode ser null)
            if (cliente.getDataNascimento() != null) {
                stmt.setDate(2, java.sql.Date.valueOf(cliente.getDataNascimento()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            // dataCadastro (pode ser null)
            if (cliente.getDataCadastro() != null) {
                stmt.setDate(3, java.sql.Date.valueOf(cliente.getDataCadastro()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }
            stmt.setInt(4, cliente.getId()); // ID do usuário criado
            stmt.setInt(5, cliente.getEndereco().getId());
            stmt.setInt(6, cliente.getTelefone().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir cliente, nenhuma linha afetada.");
            }

            // Captura o ID gerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    cliente.setId(idGerado); // Atualiza o objeto com ID
                    return idGerado;
                } else {
                    throw new SQLException("Falha ao obter ID gerado do cliente.");
                }
            }
        }
    }
}
