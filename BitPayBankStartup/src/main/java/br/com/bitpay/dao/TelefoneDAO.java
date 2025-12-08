package br.com.bitpay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.bitpay.model.Telefone;

public class TelefoneDAO {

    public Telefone buscarTelefonePorId(int id) throws SQLException {
        String sql = "SELECT id, codPais, codArea, numero FROM Telefones WHERE id = ?";
        try (Connection conn = br.com.bitpay.util.ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Telefone t = new Telefone();
                    t.setId(rs.getInt("id"));
                    t.setCodPais(rs.getInt("codPais"));
                    t.setCodArea(rs.getInt("codArea"));
                    t.setNumero(rs.getLong("numero"));
                    return t;
                }
            }
        }
        return null;
    }

    public int inserirTelefone(Connection conn, Telefone telefone) throws SQLException {
        String sql = "INSERT INTO Telefones (id, codPais, codArea, numero) VALUES (seq_Telefones.NEXTVAL, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"})) {
            ps.setInt(1, telefone.getCodPais());
            ps.setInt(2, telefone.getCodArea());
            ps.setLong(3, telefone.getNumero());
            int affected = ps.executeUpdate();
            if (affected == 0) throw new SQLException("Falha ao inserir telefone.");
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    telefone.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Não foi possível obter ID do telefone inserido.");
    }

    public boolean atualizarTelefone(Connection conn, Telefone telefone) throws SQLException {
        if (telefone.getId() == 0) {
            int novo = inserirTelefone(conn, telefone);
            return novo > 0;
        }
        String sql = "UPDATE Telefones SET codPais = ?, codArea = ?, numero = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, telefone.getCodPais());
            ps.setInt(2, telefone.getCodArea());
            ps.setLong(3, telefone.getNumero());
            ps.setInt(4, telefone.getId());
            return ps.executeUpdate() > 0;
        }
    }
}
