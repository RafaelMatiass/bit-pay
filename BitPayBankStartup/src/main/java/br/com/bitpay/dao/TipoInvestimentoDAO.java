package br.com.bitpay.dao;

import br.com.bitpay.model.TipoInvestimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoInvestimentoDAO {

    private static final String SELECT_ALL =
        "SELECT id, nome, rentabilidadeMes, carenciaDias, valorMinimo " +
        "FROM TiposInvestimento ORDER BY nome";

    private static final String SELECT_BY_ID =
        "SELECT id, nome, rentabilidadeMes, carenciaDias, valorMinimo " +
        "FROM TiposInvestimento WHERE id = ?";

    public List<TipoInvestimento> listarTodos(Connection conn) throws Exception {

        List<TipoInvestimento> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new TipoInvestimento(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getBigDecimal("rentabilidadeMes"),
                    rs.getInt("carenciaDias"),
                    rs.getBigDecimal("valorMinimo")
                ));
            }
        }
        return lista;
    }

    public TipoInvestimento buscarTipoInvestimentoPorId(Connection conn, int id)
            throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoInvestimento(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getBigDecimal("rentabilidadeMes"),
                        rs.getInt("carenciaDias"),
                        rs.getBigDecimal("valorMinimo")
                    );
                }
            }
        }
        return null;
    }
}
