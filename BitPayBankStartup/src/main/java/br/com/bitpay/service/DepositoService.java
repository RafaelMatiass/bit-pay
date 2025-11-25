package br.com.bitpay.service;

import br.com.bitpay.util.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.math.BigDecimal;

public class DepositoService {

    public void realizarDeposito(int idConta, BigDecimal valor) throws Exception {
        
        Connection conn = null;
        String callDeposito = "{ call PR_REALIZAR_DEPOSITO(?, ?) }";

        try {
            conn = ConnectionFactory.getConnection();
            
            try (CallableStatement cstmt = conn.prepareCall(callDeposito)) {
                
                cstmt.setInt(1, idConta); 
                cstmt.setBigDecimal(2, valor);

                cstmt.execute(); 
            }
            
        } catch (SQLException e) {
            throw new Exception("Falha no depósito: " + e.getMessage(), e);
        } finally {
            conn.close();
        }
    }
}