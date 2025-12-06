package br.com.bitpay.dao;


import br.com.bitpay.model.AplicacaoInvestimento;
import br.com.bitpay.model.TipoInvestimento;
import br.com.bitpay.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class InvestimentoDAO {

    
    private TipoInvestimento mapearTipoInvestimento(ResultSet rs) throws SQLException {
        return new TipoInvestimento(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getBigDecimal("rentabilidadeMes"),
            rs.getInt("carenciaDias"),
            rs.getBigDecimal("valorMinimo")
        );
    }
    
    private AplicacaoInvestimento mapearAplicacao(ResultSet rs) throws SQLException {
       
        TipoInvestimento tipo = this.buscarTipoInvestimentoPorId(rs.getInt("idTipoInvestimento"));

        LocalDate dataAplicacao = rs.getDate("dataAplicacao").toLocalDate();
        return new AplicacaoInvestimento(
            rs.getInt("id"),
            rs.getInt("idConta"),
            tipo, 
            rs.getBigDecimal("valorAplicado"),
            dataAplicacao,
            rs.getString("status")
        );
    }
    
    public List<AplicacaoInvestimento> listarAplicacoesPorConta(int idConta) throws SQLException {
        List<AplicacaoInvestimento> aplicacoes = new ArrayList<>();
        
        String sql = "SELECT ap.ID, ap.IDCONTA, ap.IDTIPOINVESTIMENTO, ap.VALORAPLICADO, ap.DATAAPLICACAO, ap.STATUS " +
                     "FROM AplicacoesInvestimentos ap WHERE ap.IDCONTA = ? AND ap.STATUS = 'ATIVA' ORDER BY ap.DATAAPLICACAO DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idConta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    aplicacoes.add(mapearAplicacao(rs));
                }
            }
        }
        return aplicacoes;
    }

    
    public List<TipoInvestimento> listarTiposInvestimento() throws SQLException {
        List<TipoInvestimento> tipos = new ArrayList<>();
        String sql = "SELECT id, nome, rentabilidadeMes, carenciaDias, valorMinimo FROM TiposInvestimento ORDER BY NOME";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tipos.add(mapearTipoInvestimento(rs));
            }
        }
        return tipos;
    }
    
    
    public TipoInvestimento buscarTipoInvestimentoPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, rentabilidadeMes, carenciaDias, valorMinimo FROM TiposInvestimento WHERE ID = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTipoInvestimento(rs);
                }
            }
        }
        return null;
    }
    
    public AplicacaoInvestimento buscarAplicacaoPorId(Connection conn, int idAplicacao) throws SQLException {
        String sql = "SELECT ID, IDCONTA, IDTIPOINVESTIMENTO, VALORAPLICADO, DATAAPLICACAO, STATUS FROM AplicacoesInvestimentos WHERE ID = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idAplicacao);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearAplicacao(rs);
                }
            }
        }
        return null;
    }
    
    
    public void resgatarAplicacao(Connection conn, int idAplicacao) throws SQLException {
        String sql = "UPDATE AplicacoesInvestimentos SET STATUS = 'RESGATADA' WHERE ID = ? AND STATUS = 'ATIVA'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAplicacao);
            
            if (stmt.executeUpdate() == 0) {
                 throw new SQLException("Falha ao resgatar aplicação ou status já está RESGATADA.");
            }
        }
    }

    
    public void aplicarInvestimento(Connection conn, AplicacaoInvestimento aplicacao) throws SQLException {
        
        String sql = "INSERT INTO AplicacoesInvestimentos (id, valorAplicado, dataAplicacao, idConta, idTipoInvestimento, status) " +
                     "VALUES (seq_AplicacoesInvestimentos.NEXTVAL,?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, aplicacao.getValorAplicado());
            Date sqlDate = Date.valueOf(aplicacao.getDataAplicacao());
            stmt.setDate(2, sqlDate);
              
            stmt.setInt(3, aplicacao.getIdConta());
            
            stmt.setInt(4, aplicacao.getTipoInvestimento().getId());
            
            stmt.setString(5, aplicacao.getStatus());
            
            stmt.executeUpdate();
        }
    }
    
 
}