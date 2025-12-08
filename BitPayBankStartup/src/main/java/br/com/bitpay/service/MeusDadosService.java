package br.com.bitpay.service;

import java.sql.Connection;

import br.com.bitpay.dao.ClienteDAO;
import br.com.bitpay.dao.EnderecoDAO;
import br.com.bitpay.dao.TelefoneDAO;
import br.com.bitpay.dao.UsuarioDAO;
import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Usuario;
import br.com.bitpay.util.ConnectionFactory;

public class MeusDadosService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final EnderecoDAO enderecoDAO = new EnderecoDAO();
    private final TelefoneDAO telefoneDAO = new TelefoneDAO();

    /**
     * Atualiza dados em uma transação.
     *
     * @param cliente domínio com os novos dados
     * @param alterarSenha true se deve alterar senha (usar nova senha de cliente.getSenha())
     */
    public void atualizarDadosCompletos(Cliente cliente, boolean alterarSenha) throws Exception {
        try (Connection conn = ConnectionFactory.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);

                // Usuario: se alterarSenha true -> atualizar email+senha; senão apenas email
                Usuario u = new Usuario();
                u.setId(cliente.getId()); // id do usuário
                u.setEmail(cliente.getEmail());
                if (alterarSenha) {
                    if (cliente.getSenha() == null || cliente.getSenha().trim().isEmpty()) {
                        throw new IllegalArgumentException("Senha nova vazia.");
                    }
                    u.setSenha(cliente.getSenha());
                    usuarioDAO.atualizarEmailSenha(conn, u);
                } else {
                    usuarioDAO.atualizarEmail(conn, u);
                }

                // Cliente (nome, dataNascimento)
                clienteDAO.atualizarCliente(conn, cliente);

                // Endereco (insert ou update)
                if (cliente.getEndereco() != null) {
                    enderecoDAO.atualizarEndereco(conn, cliente.getEndereco());
                }

                // Telefone (insert ou update)
                if (cliente.getTelefone() != null) {
                    telefoneDAO.atualizarTelefone(conn, cliente.getTelefone());
                }

                conn.commit();
            } catch (Exception e) {
                try { conn.rollback(); } catch (Exception ignored) {}
                throw e;
            } finally {
                conn.setAutoCommit(oldAuto);
            }
        }
    }
}
