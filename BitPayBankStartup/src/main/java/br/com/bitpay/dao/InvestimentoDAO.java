package br.com.bitpay.dao;

import br.com.bitpay.model.AplicacaoInvestimento;
import br.com.bitpay.model.TipoInvestimento;
import br.com.bitpay.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvestimentoDAO {

    /* ======================================
       MAPEAMENTOS
       ====================================== */

    private TipoInvestimento mapearTipo(ResultSet rs) throws SQLException {
        return new TipoInvestimento(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getBigDecimal("rentabilidadeMes"),
                rs.getInt("carenciaDias"),
                rs.getBigDecimal("valorMinimo")
        );
    }

    private AplicacaoInvestimento mapearAplicacao(ResultSet rs) throws SQLException {
        TipoInvestimento tipo = buscarTipoInvestimentoPorId(
                rs.getInt("idTipoInvestimento")
        );

        return new AplicacaoInvestimento(
                rs.getInt("id"),
                rs.getInt("idConta"),
                tipo,
                rs.getBigDecimal("valorAplicado"),
                rs.getDate("dataAplicacao").toLocalDate(),
                rs.getString("status")
        );
    }

    /* ======================================
       TIPOS DE INVESTIMENTO
       ====================================== */

    public List<TipoInvestimento> listarTiposInvestimento() throws SQLException {

        List<TipoInvestimento> lista = new ArrayList<>();

        String sql =
            "SELECT id, nome, rentabilidadeMes, carenciaDias, valorMinimo " +
            "FROM TiposInvestimento ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearTipo(rs));
            }
        }
        return lista;
    }

    public TipoInvestimento buscarTipoInvestimentoPorId(int id) throws SQLException {

        String sql =
            "SELECT id, nome, rentabilidadeMes, carenciaDias, valorMinimo " +
            "FROM TiposInvestimento WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTipo(rs);
                }
            }
        }
        return null;
    }

    /* ======================================
       APLICAÇÕES
       ====================================== */

    public void aplicarInvestimento(Connection conn, AplicacaoInvestimento aplicacao)
            throws SQLException {

        String sql =
            "INSERT INTO AplicacoesInvestimentos " +
            "(id, valorAplicado, dataAplicacao, idConta, idTipoInvestimento, status) " +
            "VALUES (seq_AplicacoesInvestimentos.NEXTVAL, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, aplicacao.getValorAplicado());
            ps.setDate(2, Date.valueOf(aplicacao.getDataAplicacao()));
            ps.setInt(3, aplicacao.getIdConta());
            ps.setInt(4, aplicacao.getTipoInvestimento().getId());
            ps.setString(5, aplicacao.getStatus());
            ps.executeUpdate();
        }
    }

    public List<AplicacaoInvestimento> listarAplicacoesPorConta(int idConta)
            throws SQLException {

        List<AplicacaoInvestimento> lista = new ArrayList<>();

        String sql =
            "SELECT id, idConta, idTipoInvestimento, valorAplicado, dataAplicacao, status " +
            "FROM AplicacoesInvestimentos " +
            "WHERE idConta = ? AND status = 'ATIVA' " +
            "ORDER BY dataAplicacao DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idConta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAplicacao(rs));
                }
            }
        }
        return lista;
    }

    public AplicacaoInvestimento buscarAplicacaoPorId(Connection conn, int idAplicacao)
            throws SQLException {

        String sql =
            "SELECT id, idConta, idTipoInvestimento, valorAplicado, dataAplicacao, status " +
            "FROM AplicacoesInvestimentos WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAplicacao);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAplicacao(rs);
                }
            }
        }
        return null;
    }

    public void resgatarAplicacao(Connection conn, int idAplicacao)
            throws SQLException {

        String sql =
            "UPDATE AplicacoesInvestimentos " +
            "SET status = 'RESGATADA' " +
            "WHERE id = ? AND status = 'ATIVA'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAplicacao);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Aplicação não encontrada ou já resgatada.");
            }
        }
    }
}
