package br.com.bitpay.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import br.com.bitpay.dao.AplicacaoInvestimentoDAO;
import br.com.bitpay.dao.ContaDAO;
import br.com.bitpay.dao.TipoInvestimentoDAO;
import br.com.bitpay.model.AplicacaoInvestimento;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.TipoInvestimento;
import br.com.bitpay.util.ConnectionFactory;

public class InvestimentoServiceImpl implements InvestimentoService {

    private final TipoInvestimentoDAO tipoDAO = new TipoInvestimentoDAO();
    private final AplicacaoInvestimentoDAO aplicacaoDAO = new AplicacaoInvestimentoDAO();
    private final ContaDAO contaDAO = new ContaDAO();

    @Override
    public List<TipoInvestimento> listarTiposInvestimento() throws Exception {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return tipoDAO.listarTodos(conn);
        }
    }

    @Override
    public List<AplicacaoInvestimento> listarPortfolio(int idConta) throws Exception {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return aplicacaoDAO.listarPorConta(conn, idConta);
        }
    }

    @Override
    public Conta buscarContaPorIdUsuario(int idUsuario) throws Exception {
        throw new UnsupportedOperationException(
            "buscarContaPorIdUsuario não é utilizado no módulo de investimentos."
        );
    }

    @Override
    public void aplicarInvestimento(int idConta, int idTipoInvestimento,
                                    BigDecimal valor) throws Exception {

        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            // 🔹 Débito do saldo
            contaDAO.ajustarSaldo(conn, idConta, valor.negate());

            // 🔹 Cria aplicação
            aplicacaoDAO.aplicar(conn, idConta, idTipoInvestimento, valor);

            conn.commit();

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    public BigDecimal resgatarInvestimento(int idAplicacao, int idConta) throws Exception {

        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            AplicacaoInvestimento aplicacao =
                    aplicacaoDAO.buscarAplicacaoPorId(conn, idAplicacao);

            if (aplicacao == null) {
                throw new Exception("Aplicação não encontrada.");
            }

            if (aplicacao.getIdConta() != idConta) {
                throw new Exception("Aplicação não pertence a esta conta.");
            }

            if (!"ATIVA".equals(aplicacao.getStatus())) {
                throw new Exception("Aplicação já foi resgatada.");
            }

            // 🔹 Marca aplicação como resgatada
            aplicacaoDAO.resgatarAplicacao(conn, idAplicacao);

            // 🔹 CREDITA O SALDO NA CONTA
            contaDAO.ajustarSaldo(conn, idConta, aplicacao.getValorAplicado());

            conn.commit();

            return aplicacao.getValorAplicado();

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }
}
