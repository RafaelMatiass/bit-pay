package br.com.bitpay.dao;

import br.com.bitpay.model.Gerente;

import java.sql.*;

public class GerenteDAO {
    
	public Gerente buscarGerentePorIdUsuario(int idUsuario, Connection conn) throws SQLException {

        String sql = "SELECT id, nome, dataNascimento FROM Gerentes WHERE idUsuario = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Gerente gerente = new Gerente();
                gerente.setId(rs.getInt("id")); 
                gerente.setNome(rs.getString("nome"));
                
                Date sqlDate = rs.getDate("dataNascimento");
                if (sqlDate != null) {
                    gerente.setDataNascimento(sqlDate.toLocalDate()); 
                }
                
                return gerente;
            }
        }
        return null;
    }
    
}