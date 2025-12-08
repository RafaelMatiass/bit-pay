package br.com.bitpay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.bitpay.model.Endereco;

public class EnderecoDAO {

    public Endereco buscarEnderecoPorId(int id) throws SQLException {
        String sql = "SELECT id, logradouro, numero, complemento, bairro, cidade, estado, cep FROM Enderecos WHERE id = ?";
        try (Connection conn = br.com.bitpay.util.ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Endereco e = new Endereco();
                    e.setId(rs.getInt("id"));
                    e.setLogradouro(rs.getString("logradouro"));
                    e.setNumero(rs.getInt("numero"));
                    e.setComplemento(rs.getString("complemento"));
                    e.setBairro(rs.getString("bairro"));
                    e.setCidade(rs.getString("cidade"));
                    e.setEstado(rs.getString("estado"));
                    e.setCep(rs.getString("cep"));
                    return e;
                }
            }
        }
        return null;
    }

    public int inserirEndereco(Connection conn, Endereco endereco) throws SQLException {
        String sql = "INSERT INTO Enderecos (id, logradouro, numero, complemento, bairro, cidade, estado, cep) " +
                     "VALUES (seq_Enderecos.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"})) {
            ps.setString(1, endereco.getLogradouro());
            ps.setInt(2, endereco.getNumero());
            ps.setString(3, endereco.getComplemento());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getCidade());
            ps.setString(6, endereco.getEstado());
            ps.setString(7, endereco.getCep());
            int affected = ps.executeUpdate();
            if (affected == 0) throw new SQLException("Falha ao inserir endereço.");
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    endereco.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Não foi possível obter ID do endereço inserido.");
    }

    public boolean atualizarEndereco(Connection conn, Endereco endereco) throws SQLException {
        // Se o endereço não tem id, faz insert e atualiza o objeto (retorna true)
        if (endereco.getId() == 0) {
            int novoId = inserirEndereco(conn, endereco);
            return novoId > 0;
        }

        String sql = "UPDATE Enderecos SET logradouro = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, estado = ?, cep = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, endereco.getLogradouro());
            ps.setInt(2, endereco.getNumero());
            ps.setString(3, endereco.getComplemento());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getCidade());
            ps.setString(6, endereco.getEstado());
            ps.setString(7, endereco.getCep());
            ps.setInt(8, endereco.getId());
            return ps.executeUpdate() > 0;
        }
    }
}
