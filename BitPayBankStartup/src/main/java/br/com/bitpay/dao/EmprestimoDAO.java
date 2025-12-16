package br.com.bitpay.dao;

import br.com.bitpay.model.Emprestimo;
import br.com.bitpay.model.Enums.StatusEmprestimo;
import br.com.bitpay.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class EmprestimoDAO {

    private static final String INSERT_SQL = 
        "INSERT INTO EMPRESTIMOS (id, valorTotal, valorEntrada, dataContratacao, numerosParcelas, taxaJuros, idConta, idStatusEmprestimo) " +
        "VALUES (seq_Emprestimos.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GENERATED_KEYS[] = {"ID"};
    
    private Emprestimo mapearEmprestimo(ResultSet rs) throws SQLException {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(rs.getInt("id"));
        emprestimo.setValorTotal(rs.getBigDecimal("valorTotal"));
        emprestimo.setValorEntrada(rs.getBigDecimal("valorEntrada"));
        
        Date sqlDateContratacao = rs.getDate("dataContratacao");
        if (sqlDateContratacao != null) {
            emprestimo.setDataContratacao(sqlDateContratacao.toLocalDate());
        }
        
        emprestimo.setNumerosParcelas(rs.getInt("numerosParcelas"));
        emprestimo.setTaxaJuros(new BigDecimal(rs.getFloat("taxaJuros")));
        emprestimo.setIdConta(rs.getInt("idConta"));
        emprestimo.setStatus(StatusEmprestimo.getByCodigo(rs.getInt("idStatusEmprestimo")));
        
        return emprestimo;
    }


    public int inserir(Connection conn, Emprestimo emprestimo) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, GENERATED_KEYS)) {
            
            stmt.setBigDecimal(1, emprestimo.getValorTotal());
            stmt.setBigDecimal(2, emprestimo.getValorEntrada());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataContratacao()));
            stmt.setInt(4, emprestimo.getNumerosParcelas());
            stmt.setFloat(5, emprestimo.getTaxaJuros().floatValue()); 
            stmt.setInt(6, emprestimo.getIdConta());
            stmt.setInt(7, emprestimo.getStatus().getCodigo()); 

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    emprestimo.setId(idGerado); 
                    return idGerado;
                }
            }
            throw new SQLException("Falha ao obter ID gerado do empréstimo.");
        }
        
        
    }
    
    public List<Emprestimo> buscarEmprestimosPorConta(int idConta) throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM EMPRESTIMOS WHERE idConta = ? AND idStatusEmprestimo IN (?, ?)"; 
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idConta);
            stmt.setInt(2, StatusEmprestimo.ATIVO.getCodigo());
            stmt.setInt(3, StatusEmprestimo.ATRASADO.getCodigo());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprestimos.add(mapearEmprestimo(rs));
                }
            }
        }
        return emprestimos;
    }
    
    public Emprestimo buscarEmprestimoPorId(int idEmprestimo) throws SQLException {
        String sql = "SELECT * FROM EMPRESTIMOS WHERE ID = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEmprestimo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEmprestimo(rs);
                }
            }
        }
        return null;
    }
    
}