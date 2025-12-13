package br.com.bitpay.service;

import br.com.bitpay.dao.ClienteDAO;
import br.com.bitpay.model.Cliente;
import br.com.bitpay.util.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

public class GerenteService {

    private ClienteDAO clienteDAO = new ClienteDAO();

    // STATUS DO CLIENTE
    private static final int STATUS_PENDENTE = 1;
    private static final int STATUS_APROVADO = 2;
    private static final int STATUS_RECUSADO = 3;

    // LISTA — PENDENTES
    public List<Cliente> buscarPendentes() throws Exception {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return clienteDAO.listarClientesPorStatus(STATUS_PENDENTE, conn);
        }
    }

    // LISTA — APROVADOS
    public List<Cliente> buscarAprovados() throws Exception {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return clienteDAO.listarClientesPorStatus(STATUS_APROVADO, conn);
        }
    }

    // LISTA — RECUSADOS
    public List<Cliente> buscarRecusados() throws Exception {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return clienteDAO.listarClientesPorStatus(STATUS_RECUSADO, conn);
        }
    }

    // APROVAR CONTA
    public void aprovarConta(int idConta) throws Exception {
        atualizarStatusConta(idConta, STATUS_APROVADO);
    }

    // RECUSAR CONTA
    public void recusarConta(int idConta) throws Exception {
        atualizarStatusConta(idConta, STATUS_RECUSADO);
    }

    // MÉTODO CENTRAL PARA ALTERAR STATUS
    private void atualizarStatusConta(int idConta, int status) throws Exception {
        String sql = "{ call PR_ATUALIZAR_STATUS_CONTA(?, ?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idConta);
            stmt.setInt(2, status);
            stmt.execute();

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar status da conta: " + e.getMessage(), e);
        }
    }
}
