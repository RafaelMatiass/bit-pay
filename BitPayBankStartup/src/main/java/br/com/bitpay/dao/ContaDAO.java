package br.com.bitpay.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.Enums.StatusConta;
import br.com.bitpay.util.ConnectionFactory;

public class ContaDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public void inserirConta(Connection conn, Conta conta) throws SQLException {
        String sql = """
            INSERT INTO Contas
            (id, numeroConta, saldo, dataAbertura, idStatusConta, idCliente)
            VALUES (seq_Contas.NEXTVAL, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, conta.getNumeroConta());
            stmt.setBigDecimal(2, conta.getSaldo());
            stmt.setDate(3, Date.valueOf(conta.getDataAbertura()));
            stmt.setInt(4, conta.getStatusConta().getCodigo());

            // ✅ CORREÇÃO CRÍTICA: FK usa CLIENTES.ID
            stmt.setInt(5, conta.getCliente().getClienteId());

            stmt.executeUpdate();
        }
    }

    public void atualizarSaldo(Connection conn, int idConta, BigDecimal valor) throws SQLException {
        String sql = "UPDATE Contas SET saldo = saldo + ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, valor);
            stmt.setInt(2, idConta);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException(
                    "Erro: Nenhuma conta encontrada com o ID " + idConta
                );
            }
        }
    }

    public Conta buscarContaPorId(int id) throws SQLException {
        String sql = """
            SELECT id, numeroConta, saldo, dataAbertura, idStatusConta, idCliente
            FROM Contas
            WHERE id = ?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idCliente = rs.getInt("idCliente");

                    Cliente cliente = clienteDAO.buscarClientePorId(idCliente);

                    return new Conta(
                        rs.getInt("id"),
                        rs.getString("numeroConta"),
                        rs.getBigDecimal("saldo"),
                        rs.getDate("dataAbertura").toLocalDate(),
                        StatusConta.getByCodigo(rs.getInt("idStatusConta")),
                        cliente
                    );
                }
            }
        }
        return null;
    }
    
    public BigDecimal buscarSaldoContaPorId(Connection conn, int idConta) throws SQLException {
        String sql = "SELECT saldo FROM Contas WHERE id = ?";
        

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idConta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("saldo");
                }
            }
        }
        return BigDecimal.ZERO; 
    }
}
