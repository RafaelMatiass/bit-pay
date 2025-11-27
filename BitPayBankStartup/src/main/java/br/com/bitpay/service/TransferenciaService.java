package br.com.bitpay.service;

import br.com.bitpay.util.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.math.BigDecimal;

public class TransferenciaService {
	
    public void realizarTransferencia(int idContaOrigem, String numeroContaDestino, BigDecimal valor) throws Exception {
        
        Connection conn = null;
        String callTransferencia = "{ call PR_REALIZAR_TRANSFERENCIA(?, ?, ?) }";

        try {
            conn = ConnectionFactory.getConnection();
            
            try (CallableStatement cstmt = conn.prepareCall(callTransferencia)) {
                
                cstmt.setInt(1, idContaOrigem); 
                cstmt.setString(2, numeroContaDestino);
                cstmt.setBigDecimal(3, valor);

                cstmt.execute(); 
            }
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 20006) {
                throw new Exception("Saldo insuficiente para cobrir o valor da transferência.");
            }
            if (e.getErrorCode() == 20007) {
                throw new Exception("Conta de destino não encontrada. Verifique o número.");
            }
            throw new Exception("Falha na transação de transferência: " + e.getMessage(), e);
        } finally {
            conn.close(); 
        }
    }
}