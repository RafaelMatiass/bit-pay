package br.com.bitpay.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.bitpay.model.Movimentacao;
import br.com.bitpay.model.Enums.TipoMovimento;
import br.com.bitpay.util.ConnectionFactory;

public class MovimentacaoDAO {

	public List<Movimentacao> buscarPorContaEPeriodo(int idConta, LocalDate dataInicial, LocalDate dataFinal) {
        List<Movimentacao> extrato = new ArrayList<>();
        LocalDate dataExclusiva = dataFinal.plusDays(1);
        
        String sql = "SELECT m.id, m.valor, m.dataMovimento, m.idConta, m.idContaDestino, m.idTipoMovimento, " +
                "c.numeroConta as numeroContaDestino, " +
                "cl.nome as nomeClienteDestino " +
                "FROM Movimentacoes m " +
                "LEFT JOIN Contas c ON m.idContaDestino = c.id " +
                "LEFT JOIN Clientes cl ON c.idCliente = cl.id " +
                "WHERE m.idConta = ? " +
                "AND m.dataMovimento >= ? AND m.dataMovimento < ? " +
                "ORDER BY m.dataMovimento DESC";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idConta);
            stmt.setDate(2, Date.valueOf(dataInicial));
            stmt.setDate(3, Date.valueOf(dataExclusiva)); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Movimentacao m = new Movimentacao();
                    m.setId(rs.getInt("id"));
                    m.setValor(rs.getBigDecimal("valor"));
                    m.setDataMovimento(rs.getDate("dataMovimento").toLocalDate());
                    m.setIdConta(rs.getInt("idConta"));
                    
                    int idContaDestino = rs.getInt("idContaDestino");
                    if (!rs.wasNull()) {
                        m.setIdContaDestino(idContaDestino);
                        
                        m.setNumeroContaDestino(rs.getString("NUMEROCONTADESTINO"));
                        m.setNomeClienteDestino(rs.getString("NOMECLIENTEDESTINO"));
                    }
                    
                    m.setTipoMovimento(TipoMovimento.fromId(rs.getInt("idTipoMovimento")));
                    extrato.add(m);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return extrato;
    }
	
	public int inserir(Connection conn, Movimentacao mov) throws SQLException {

	    String sql = "INSERT INTO Movimentacoes " +
	                 "(id, valor, dataMovimento, idConta, idContaDestino, idTipoMovimento) " +
	                 "VALUES (seq_Movimentacoes.NEXTVAL, ?, ?, ?, ?, ?)";

	    try (PreparedStatement stmt = conn.prepareStatement(sql, new String[] {"ID"})) {

	        stmt.setBigDecimal(1, mov.getValor());
	        stmt.setDate(2, Date.valueOf(mov.getDataMovimento()));
	        stmt.setInt(3, mov.getIdConta());

	     
	        if (mov.getIdContaDestino() == null) {
	            stmt.setNull(4, Types.INTEGER);
	        } else {
	            stmt.setInt(4, mov.getIdContaDestino());
	        }

	        stmt.setInt(5, mov.getTipoMovimento().getId()); 

	        int rows = stmt.executeUpdate();

	        if (rows == 0) {
	            throw new SQLException("Falha ao inserir movimentação — nenhuma linha afetada.");
	        }

	        try (ResultSet rs = stmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                return rs.getInt(1); 
	            } else {
	                throw new SQLException("Falha ao recuperar ID da movimentação.");
	            }
	        }
	    }
	}
}