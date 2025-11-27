package br.com.bitpay.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}