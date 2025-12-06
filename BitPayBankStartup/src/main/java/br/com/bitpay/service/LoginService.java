package br.com.bitpay.service;

import br.com.bitpay.dao.ClienteDAO;
import br.com.bitpay.dao.GerenteDAO;
import br.com.bitpay.dao.UsuarioDAO;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Gerente;
import br.com.bitpay.model.Usuario;
import br.com.bitpay.util.ConnectionFactory;
import br.com.bitpay.model.Enums.TipoUsuario;

import java.sql.Connection;

public class LoginService {

	private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private GerenteDAO gerenteDAO = new GerenteDAO();

public Usuario autenticar(String email, String senha) throws Exception {
        
        try (Connection conn = ConnectionFactory.getConnection()) {
            
            Usuario baseUser = usuarioDAO.buscarUsuarioBase(email, senha, conn);
            
            if (baseUser == null) {
                throw new Exception("Email ou senha inválidos.");
            }
            
            if (baseUser.getTipoUsuario() == TipoUsuario.CLIENTE) {
                
                Cliente cliente = clienteDAO.buscarClientePorIdUsuario(baseUser.getId(), conn);
                
                if (cliente != null) {
                    copiarDadosBase(baseUser, cliente);
                    return cliente;
                } else {
                    throw new Exception("Dados de cliente incompletos ou não encontrados.");
                }
                
            } else if (baseUser.getTipoUsuario() == TipoUsuario.GERENTE) {
                
                Gerente gerente = gerenteDAO.buscarGerentePorIdUsuario(baseUser.getId(), conn);

                if (gerente != null) {
                    copiarDadosBase(baseUser, gerente);
                    return gerente;
                } else {
                    throw new Exception("Dados de gerente incompletos ou não encontrados.");
                }

            } else {
                throw new Exception("Perfil de usuário inválido.");
            }
        }
    }
    
    private void copiarDadosBase(Usuario origem, Usuario destino) {
        destino.setId(origem.getId());
        destino.setCpf(origem.getCpf());
        destino.setEmail(origem.getEmail());
        destino.setSenha(origem.getSenha());
        destino.setTipoUsuario(origem.getTipoUsuario());
    }
	
}