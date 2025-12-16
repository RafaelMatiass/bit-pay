package br.com.bitpay.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.com.bitpay.model.AplicacaoInvestimento;
import br.com.bitpay.model.TipoInvestimento;

public class AplicacaoInvestimentoDAO {

    private static final String SELECT_POR_CONTA =
        "SELECT ai.id, ai.valorAplicado, ai.dataAplicacao, ai.status, ai.idConta, " +
        "ti.id AS tipoId, ti.nome, ti.rentabilidadeMes, ti.carenciaDias, ti.valorMinimo " +
        "FROM AplicacoesInvestimentos ai " +
        "JOIN TiposInvestimento ti ON ti.id = ai.idTipoInvestimento " +
        "WHERE ai.idConta = ? AND ai.status = 'ATIVA' " +
        "ORDER BY ai.dataAplicacao DESC";

    private static final String INSERT =
        "INSERT INTO AplicacoesInvestimentos " +
        "(id, valorAplicado, dataAplicacao, idConta, idTipoInvestimento, status) " +
        "VALUES (seq_AplicacoesInvestimentos.NEXTVAL, ?, SYSDATE, ?, ?, 'ATIVA')";

    private static final String SELECT_POR_ID =
        "SELECT ai.id, ai.valorAplicado, ai.dataAplicacao, ai.status, ai.idConta, " +
        "ti.id AS tipoId, ti.nome, ti.rentabilidadeMes, ti.carenciaDias, ti.valorMinimo " +
        "FROM AplicacoesInvestimentos ai " +
        "JOIN TiposInvestimento ti ON ti.id = ai.idTipoInvestimento " +
        "WHERE ai.id = ?";

    private static final String UPDATE_RESGATAR =
        "UPDATE AplicacoesInvestimentos SET status = 'RESGATADA' " +
        "WHERE id = ? AND status = 'ATIVA'";

    public List<AplicacaoInvestimento> listarPorConta(Connection conn, int idConta)
            throws SQLException {

        List<AplicacaoInvestimento> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(SELECT_POR_CONTA)) {
            ps.setInt(1, idConta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    TipoInvestimento tipo = new TipoInvestimento(
                        rs.getInt("tipoId"),
                        rs.getString("nome"),
                        rs.getBigDecimal("rentabilidadeMes"),
                        rs.getInt("carenciaDias"),
                        rs.getBigDecimal("valorMinimo")
                    );

                    AplicacaoInvestimento a = new AplicacaoInvestimento();
                    a.setId(rs.getInt("id"));
                    a.setIdConta(rs.getInt("idConta"));
                    a.setValorAplicado(rs.getBigDecimal("valorAplicado"));
                    a.setDataAplicacao(rs.getDate("dataAplicacao").toLocalDate());
                    a.setStatus(rs.getString("status"));
                    a.setTipoInvestimento(tipo);

                    lista.add(a);
                }
            }
        }
        return lista;
    }

    public void aplicar(Connection conn, int idConta, int idTipo, BigDecimal valor)
            throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setBigDecimal(1, valor);
            ps.setInt(2, idConta);
            ps.setInt(3, idTipo);
            ps.executeUpdate();
        }
    }

    public AplicacaoInvestimento buscarAplicacaoPorId(Connection conn, int idAplicacao)
            throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {
            ps.setInt(1, idAplicacao);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    TipoInvestimento tipo = new TipoInvestimento(
                        rs.getInt("tipoId"),
                        rs.getString("nome"),
                        rs.getBigDecimal("rentabilidadeMes"),
                        rs.getInt("carenciaDias"),
                        rs.getBigDecimal("valorMinimo")
                    );

                    AplicacaoInvestimento a = new AplicacaoInvestimento();
                    a.setId(rs.getInt("id"));
                    a.setIdConta(rs.getInt("idConta"));
                    a.setValorAplicado(rs.getBigDecimal("valorAplicado"));
                    a.setDataAplicacao(rs.getDate("dataAplicacao").toLocalDate());
                    a.setStatus(rs.getString("status"));
                    a.setTipoInvestimento(tipo);

                    return a;
                }
            }
        }
        return null;
    }

    public void resgatarAplicacao(Connection conn, int idAplicacao)
            throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(UPDATE_RESGATAR)) {
            ps.setInt(1, idAplicacao);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Aplicação não encontrada ou já resgatada.");
            }
        }
    }
}
