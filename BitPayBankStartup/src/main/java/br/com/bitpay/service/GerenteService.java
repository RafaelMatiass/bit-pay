package br.com.bitpay.service;


import br.com.bitpay.dao.ClienteDAO;
import br.com.bitpay.model.Cliente;
import br.com.bitpay.util.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

public class GerenteService {
    private ClienteDAO clienteDAO = new ClienteDAO();
    
    private static final int CODIGO_PENDENTE = 1; 
    private static final int CODIGO_APROVADO = 2; 


    public List<Cliente> buscarPendentes() throws Exception {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return clienteDAO.listarClientesPorStatus(CODIGO_PENDENTE, conn);
        }
    }


    public void aprovarConta(int idConta) throws Exception {
        String call = "{ call PR_APROVAR_CONTA(?, ?) }";
        
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cstmt = conn.prepareCall(call)) {
            
            cstmt.setInt(1, idConta);
            cstmt.setInt(2, CODIGO_APROVADO);
            
            cstmt.execute();
            
        } catch (Exception e) {
            throw new Exception("Falha na aprovação da conta: " + e.getMessage(), e);
        }
    }
}