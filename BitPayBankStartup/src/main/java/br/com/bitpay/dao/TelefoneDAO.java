package br.com.bitpay.dao;

import br.com.bitpay.model.Telefone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelefoneDAO {

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
}
