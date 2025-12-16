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

    /* =========================================================
       INSERIR CONTA
       ========================================================= */
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
            stmt.setInt(5, conta.getCliente().getClienteId());
            stmt.executeUpdate();
        }
    }

    /* =========================================================
       BUSCAR SALDO (VERSÃO QUE LANÇA EXCEÇÃO)
       👉 usada em investimentos / empréstimos
       ========================================================= */
    public BigDecimal buscarSaldo(Connection conn, int idConta) throws SQLException {

        String sql = "SELECT saldo FROM Contas WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("saldo");
                }
            }
        }
        throw new SQLException("Conta não encontrada para ID: " + idConta);
    }

    /* =========================================================
       BUSCAR SALDO (VERSÃO SEGURA)
       👉 usada quando não pode quebrar fluxo
       ========================================================= */
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

    /* =========================================================
       AJUSTAR SALDO (CRÉDITO / DÉBITO)
       👉 soma ou subtrai
       👉 usado em depósito, saque, investimento, resgate
       ========================================================= */
    public void ajustarSaldo(Connection conn, int idConta, BigDecimal valor)
            throws SQLException {

        String sql = "UPDATE Contas SET saldo = saldo + ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, valor);
            stmt.setInt(2, idConta);

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Nenhuma conta encontrada com ID " + idConta);
            }
        }
    }

    /* =========================================================
       ATUALIZAR SALDO (VALOR ABSOLUTO)
       👉 usado quando o saldo final já foi calculado
       ========================================================= */
    public void atualizarSaldo(Connection conn, int idConta, BigDecimal novoSaldo)
            throws SQLException {

        String sql = "UPDATE Contas SET saldo = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, novoSaldo);
            stmt.setInt(2, idConta);

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Nenhuma conta encontrada com ID " + idConta);
            }
        }
    }

    /* =========================================================
       BUSCAR CONTA COMPLETA POR ID
       ========================================================= */
    public Conta buscarContaPorId(int idConta) throws SQLException {

        String sql = """
            SELECT id, numeroConta, saldo, dataAbertura, idStatusConta, idCliente
            FROM Contas
            WHERE id = ?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idConta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    Cliente cliente =
                        clienteDAO.buscarClientePorId(rs.getInt("idCliente"));

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
}
