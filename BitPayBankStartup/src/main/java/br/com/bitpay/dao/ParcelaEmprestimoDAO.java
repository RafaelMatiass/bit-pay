package br.com.bitpay.dao;

import br.com.bitpay.model.ParcelaEmprestimo;
import java.sql.*;
import java.util.List;

public class ParcelaEmprestimoDAO {
    
    private static final String INSERT_SQL = 
        "INSERT INTO PARCELASEMPRESTIMOS (id, numeroParcela, valorParcela, dataVencimento, idEmprestimo, idStatusParcela) " +
        "VALUES (seq_parcelasEmprestimos.NEXTVAL, ?, ?, ?, ?, ?)";
    
    public void inserirTodas(Connection conn, List<ParcelaEmprestimo> parcelas, int idEmprestimo) throws SQLException {
        
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            for (ParcelaEmprestimo parcela : parcelas) {
                stmt.setInt(1, parcela.getNumeroParcela());
                stmt.setBigDecimal(2, parcela.getValorParcela());
                stmt.setDate(3, Date.valueOf(parcela.getDataVencimento()));
                stmt.setInt(4, idEmprestimo); 
                stmt.setInt(5, parcela.getStatus().getCodigo());
                
                stmt.addBatch(); 
            }
            
            stmt.executeBatch(); 
        }
    }
}