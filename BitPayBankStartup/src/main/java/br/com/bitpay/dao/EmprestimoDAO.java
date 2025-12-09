package br.com.bitpay.dao;

import br.com.bitpay.model.Emprestimo;
import java.sql.*;

public class EmprestimoDAO {

    private static final String INSERT_SQL = 
        "INSERT INTO EMPRESTIMOS (id, valorTotal, valorEntrada, dataContratacao, numerosParcelas, taxaJuros, idConta, idStatusEmprestimo) " +
        "VALUES (seq_Emprestimos.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GENERATED_KEYS[] = {"ID"};


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
}