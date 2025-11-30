package br.com.bitpay.dao;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.Endereco;
import br.com.bitpay.model.Telefone;
import br.com.bitpay.model.Usuario;
import br.com.bitpay.model.Enums.StatusConta;
import br.com.bitpay.model.Enums.TipoUsuario;
import br.com.bitpay.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class ClienteDAO {

	private final EnderecoDAO enderecoDAO = new EnderecoDAO();
    private final TelefoneDAO telefoneDAO = new TelefoneDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO(); 
    
    public int inserir(Connection conn, Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Clientes (id, nome, dataNascimento, dataCadastro, idUsuario, idEndereco, idTelefone) " +
                     "VALUES (seq_Clientes.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {
            stmt.setString(1, cliente.getNome());
            if (cliente.getDataNascimento() != null) {
                stmt.setDate(2, java.sql.Date.valueOf(cliente.getDataNascimento()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            if (cliente.getDataCadastro() != null) {
                stmt.setDate(3, java.sql.Date.valueOf(cliente.getDataCadastro()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }
            stmt.setInt(4, cliente.getId()); 
            stmt.setInt(5, cliente.getEndereco().getId());
            stmt.setInt(6, cliente.getTelefone().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir cliente, nenhuma linha afetada.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    cliente.setId(idGerado);
                    return idGerado;
                } else {
                    throw new SQLException("Falha ao obter ID gerado do cliente.");
                }
            }
        }
    }
    
public Cliente buscarClientePorId(int id) throws SQLException {
        
       
        String sql = "SELECT c.nome, c.dataNascimento, c.dataCadastro, c.idUsuario, c.idUsuario, c.idEndereco, c.idTelefone  "
        		+ "FROM Clientes c WHERE id = ?";

      
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                   
                    int idUsuario = rs.getInt("idUsuario");
                    int idEndereco = rs.getInt("idEndereco");
                    int idTelefone = rs.getInt("idTelefone");
                    
                
                    Endereco endereco = enderecoDAO.buscarEnderecoPorId(idEndereco);
                    Telefone telefone = telefoneDAO.buscarTelefonePorId(idTelefone);
                    Usuario usuario = usuarioDAO.buscarUsuarioPorId(idUsuario);
                    
                    
                    
              
                    Cliente cliente = new Cliente(
                        usuario.getId(),
                        usuario.getCpf(),
                        usuario.getSenha(),
                        usuario.getEmail(),
                        usuario.getTipoUsuario(),
                        rs.getString("nome"),
                        rs.getDate("dataNascimento").toLocalDate(),
                        rs.getDate("dataCadastro").toLocalDate(),
                        endereco,
                        telefone);
                    
                    return cliente;
                }
            }
        }
        return null;
    }
    
public Cliente buscarCliente(String email, String senha, Connection conn) throws SQLException {
        
        String sql = 
            "SELECT U.id AS U_ID, U.CPF, U.SENHA, U.EMAIL, U.IDTIPOUSUARIO, " +
                   "C.ID AS C_ID, C.NOME, C.DATANASCIMENTO, C.DATACADASTRO, " +
                   "CO.NUMEROCONTA, CO.SALDO, CO.ID AS CO_ID, CO.IDSTATUSCONTA " +
            "FROM USUARIOS U " +
            "INNER JOIN CLIENTES C ON U.id = C.idUsuario " + 
            "INNER JOIN CONTAS CO ON C.ID = CO.ID " +    
            "WHERE U.EMAIL = ? AND U.SENHA = ?";
            
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email); 
            stmt.setString(2, senha); 

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("U_ID"));
                    usuario.setCpf(rs.getString("CPF"));
                    usuario.setSenha(rs.getString("SENHA"));
                    usuario.setEmail(rs.getString("EMAIL"));
                    
                    int codigoTipo = rs.getInt("IDTIPOUSUARIO");
                    usuario.setTipoUsuario(TipoUsuario.getByCodigo(codigoTipo));
                    
                    Conta conta = new Conta();
                    conta.setContaId(rs.getInt("CO_ID"));
                    conta.setNumeroConta(rs.getString("NUMEROCONTA"));
                    
                    conta.setSaldo(rs.getBigDecimal("SALDO")); 
                    
                    int codigoStatus = rs.getInt("IDSTATUSCONTA");
                    conta.setStatusConta(StatusConta.getByCodigo(codigoStatus));
                    
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("C_ID"));
                    cliente.setNome(rs.getString("NOME"));
                    
                    cliente.setDataNascimento(rs.getDate("DATANASCIMENTO").toLocalDate());
                    cliente.setDataCadastro(rs.getDate("DATACADASTRO").toLocalDate());
                    
                    cliente.setConta(conta); 
                    
                    return cliente;
                }
            }
        }
        return null; 
    }



}
