package br.com.bitpay.dao;

import br.com.bitpay.model.ParcelaEmprestimo;
import br.com.bitpay.model.Enums.StatusParcelasEmprestimo;
import br.com.bitpay.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ParcelaEmprestimoDAO {
    
    private static final String INSERT_SQL = 
        "INSERT INTO PARCELASEMPRESTIMOS (id, numeroParcela, valorParcela, dataVencimento, idEmprestimo, idStatusParcela, VALORAMORTIZACAO, VALORJUROS, SALDODEVEDORAPOSPAGAMENTO) " +
        "VALUES (seq_parcelasEmprestimos.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private ParcelaEmprestimo mapearParcela(ResultSet rs) throws SQLException {
        ParcelaEmprestimo parcela = new ParcelaEmprestimo();
        parcela.setId(rs.getInt("id"));
        parcela.setNumeroParcela(rs.getInt("numeroParcela"));
        parcela.setValorParcela(rs.getBigDecimal("valorParcela"));
        
        Date sqlDateVencimento = rs.getDate("dataVencimento");
        if (sqlDateVencimento != null) {
            parcela.setDataVencimento(sqlDateVencimento.toLocalDate());
        }
        
        parcela.setValorPago(rs.getBigDecimal("valorPago"));
        
        Date sqlDatePagamento = rs.getDate("dataPagamento");
        if (sqlDatePagamento != null) {
            parcela.setDataPagamento(sqlDatePagamento.toLocalDate());
        }
        
        parcela.setIdEmprestimo(rs.getInt("idEmprestimo"));
        parcela.setStatus(StatusParcelasEmprestimo.getByCodigo(rs.getInt("idStatusParcela")));
        
        parcela.setValorAmortizacao(rs.getBigDecimal("valorAmortizacao"));
        parcela.setValorJuros(rs.getBigDecimal("valorJuros"));
      
        parcela.setSaldoDevedorAtual(rs.getBigDecimal("saldoDevedorAposPagamento")); 
        
        return parcela;
    }
    
    public List<ParcelaEmprestimo> buscarParcelasPorEmprestimo(int idEmprestimo) throws SQLException {
        List<ParcelaEmprestimo> parcelas = new ArrayList<>();
        String sql = "SELECT * FROM PARCELASEMPRESTIMOS WHERE idEmprestimo = ? ORDER BY numeroParcela";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEmprestimo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    parcelas.add(mapearParcela(rs));
                }
            }
        }
        return parcelas;
    }
    
    
    public ParcelaEmprestimo buscarParcelaPorId(Connection conn, int idParcela) throws SQLException {
        String sql = "SELECT * FROM PARCELASEMPRESTIMOS WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idParcela);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearParcela(rs);
                }
            }
        }
        return null;
    }
    
    public void pagarParcela(Connection conn, int idParcela, BigDecimal valorPago) throws SQLException {
        String sql = "UPDATE PARCELASEMPRESTIMOS SET " +
                     "idStatusParcela = ?, " + 
                     "valorPago = ?, " + 
                     "dataPagamento = ? " +
                     "WHERE id = ? AND idStatusParcela = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, StatusParcelasEmprestimo.PAGA.getCodigo());
            stmt.setBigDecimal(2, valorPago);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setInt(4, idParcela);
            stmt.setInt(5, StatusParcelasEmprestimo.ABERTA.getCodigo()); 
            
            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Falha ao pagar parcela. Status incorreto ou parcela não encontrada.");
            }
        }
    }
    
    public void inserirTodas(Connection conn, List<ParcelaEmprestimo> parcelas, int idEmprestimo) throws SQLException {
        
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            for (ParcelaEmprestimo parcela : parcelas) {
                stmt.setInt(1, parcela.getNumeroParcela());
                stmt.setBigDecimal(2, parcela.getValorParcela());
                stmt.setDate(3, Date.valueOf(parcela.getDataVencimento()));
                stmt.setInt(4, idEmprestimo); 
                stmt.setInt(5, parcela.getStatus().getCodigo());
                stmt.setBigDecimal(6, parcela.getValorAmortizacao());
                stmt.setBigDecimal(7,parcela.getValorJuros());
                stmt.setBigDecimal(8, parcela.getSaldoDevedorAtual());
                
                
                stmt.addBatch(); 
            }
            
            stmt.executeBatch(); 
        }
    }
}