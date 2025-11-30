package br.com.bitpay.dao;

import br.com.bitpay.model.Endereco;
import br.com.bitpay.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnderecoDAO {
	
	private Endereco mapearEndereco(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco(
            rs.getString("LOGRADOURO"),
            rs.getInt("NUMERO"),
            rs.getString("BAIRRO"),
            rs.getString("CIDADE"),
            rs.getString("ESTADO"),
            rs.getString("CEP")
        );
        endereco.setId(rs.getInt("ID"));
        return endereco;
    }
	
    public int inserirEndereco(Connection conn, Endereco endereco) throws SQLException {
        String sql = "INSERT INTO Enderecos (id, logradouro, numero, bairro, cidade, estado, cep) " +
                     "VALUES (seq_Enderecos.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {
            stmt.setString(1, endereco.getLogradouro());
            stmt.setInt(2, endereco.getNumero());
            stmt.setString(3, endereco.getBairro());
            stmt.setString(4, endereco.getCidade());
            stmt.setString(5, endereco.getEstado());
            stmt.setString(6, endereco.getCep());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir endereço, nenhuma linha afetada.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    endereco.setId(idGerado);
                    return idGerado;
                } else {
                    throw new SQLException("Falha ao obter ID gerado do endereço.");
                }
            }
        }
    }
    
    public Endereco buscarEnderecoPorId(int id) throws SQLException {
        String sql = "SELECT id, logradouro, numero, bairro, cidade, estado, cep FROM Enderecos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    return mapearEndereco(rs);
                }
            }
        }
        return null;
    }
    
}
