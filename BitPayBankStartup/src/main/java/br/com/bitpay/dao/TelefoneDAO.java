package br.com.bitpay.dao;

import br.com.bitpay.model.Telefone;
import br.com.bitpay.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelefoneDAO {
	
	 private Telefone mapearTelefone(ResultSet rs) throws SQLException {
	        Telefone telefone = new Telefone(
	            rs.getInt("CODPais"),
	            rs.getInt("CODArea"),
	            rs.getLong("NUMERO")
	        );
	        telefone.setId(rs.getInt("ID"));
	        return telefone;
	    }

    public int inserirTelefone(Connection conn, Telefone telefone) throws SQLException {
        String sql = "INSERT INTO Telefones (id, codPais, codArea, numero) " +
                     "VALUES (seq_Telefones.NEXTVAL, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {
            stmt.setInt(1, telefone.getCodPais());
            stmt.setInt(2, telefone.getCodArea());
            stmt.setLong(3, telefone.getNumero());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir telefone, nenhuma linha afetada.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    telefone.setId(idGerado);
                    return idGerado;
                } else {
                    throw new SQLException("Falha ao obter ID gerado do telefone.");
                }
            }
        }
    }
    public Telefone buscarTelefonePorId(int id) throws SQLException {
        String sql = "SELECT id, codPais, codArea, numero FROM Telefones WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTelefone(rs);
                }
            }
        }
        return null;
    }
}
