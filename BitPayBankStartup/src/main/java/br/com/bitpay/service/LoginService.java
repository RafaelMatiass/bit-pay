package br.com.bitpay.service;

import br.com.bitpay.dao.ClienteDAO;
import br.com.bitpay.model.Cliente;
import br.com.bitpay.util.ConnectionFactory;
import br.com.bitpay.model.Enums.StatusConta; 

import java.sql.Connection;
import java.sql.SQLException;

public class LoginService {

    private ClienteDAO clienteDAO = new ClienteDAO(); 

    public Cliente autenticar(String email, String senha) throws Exception {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            
            Cliente clienteLogado = clienteDAO.buscarCliente(email, senha, conn);
            
            if (clienteLogado == null) {
                throw new Exception("Email ou Senha inválidos."); 
            }
            
            StatusConta statusAtual = clienteLogado.getConta().getStatusConta();
            
            if (statusAtual != StatusConta.PENDENTE && statusAtual != StatusConta.ATIVA) {
                throw new Exception("Sua conta está no status " + statusAtual.getDescricao() + " e não pode ser acessada. Contate o suporte.");
            }
            
            return clienteLogado;

        } catch (SQLException e) {
            throw new Exception("Erro de conexão ou consulta ao banco: " + e.getMessage(), e);
        } finally {
            conn.close();
        }
    }
}