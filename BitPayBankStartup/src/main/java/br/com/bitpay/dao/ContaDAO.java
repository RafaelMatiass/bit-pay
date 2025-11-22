package br.com.bitpay.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.bitpay.model.Conta;

public class ContaDAO {
	
	public void inserirConta(Connection conn, Conta conta) throws SQLException {
        String sql = "INSERT INTO Contas (id, numeroConta, saldo, dataAbertura, idStatusConta, idCliente) " +
                     "VALUES (seq_Contas.NEXTVAL, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
           
            stmt.setString(1, conta.getNumeroConta());
            stmt.setBigDecimal(2, conta.getSaldo());
            stmt.setDate(3, Date.valueOf(conta.getDataAbertura()));
            stmt.setInt(4, conta.getStatusConta().getCodigo());
            stmt.setInt(5, conta.getCliente().getId());
           
            stmt.executeUpdate();
        }
    }
}
