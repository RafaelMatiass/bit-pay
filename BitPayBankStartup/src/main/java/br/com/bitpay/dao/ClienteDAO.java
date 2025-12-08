package br.com.bitpay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.Endereco;
import br.com.bitpay.model.Telefone;
import br.com.bitpay.model.Usuario;
import br.com.bitpay.model.Enums.StatusConta;
import br.com.bitpay.model.Enums.TipoUsuario;
import br.com.bitpay.util.ConnectionFactory;

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
            stmt.setInt(4, cliente.getId()); // idUsuario
            stmt.setInt(5, cliente.getEndereco().getId());
            stmt.setInt(6, cliente.getTelefone().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir cliente, nenhuma linha afetada.");
            }
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    cliente.setClienteId(idGerado);
                    return idGerado;
                } else {
                    throw new SQLException("Falha ao obter ID gerado do cliente.");
                }
            }
        }
    }

    public Cliente buscarClientePorId(int id) throws SQLException {
        String sql = "SELECT c.id AS C_ID, c.nome, c.dataNascimento, c.dataCadastro, c.idUsuario, c.idEndereco, c.idTelefone " +
                     "FROM Clientes c WHERE c.id = ?";
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
                        usuario.getCpf(),
                        usuario.getSenha(),
                        usuario.getEmail(),
                        usuario.getTipoUsuario(),
                        rs.getString("nome"),
                        rs.getDate("dataNascimento").toLocalDate(),
                        rs.getDate("dataCadastro").toLocalDate(),
                        endereco,
                        telefone
                    );

                    // set IDs corretamente
                    cliente.setClienteId(rs.getInt("C_ID"));
                    cliente.setId(usuario.getId());
                    return cliente;
                }
            }
        }
        return null;
    }

    /**
     * Busca cliente + conta (versão usada pelo fluxo Meus Dados que também mostra saldo/conta).
     * Retorna cliente com conta (se houver).
     */
    public Cliente buscarClientePorIdUsuario(int idUsuario, Connection conn) throws SQLException {
        String sql =
            "SELECT " +
            "  C.ID AS C_ID, C.NOME, C.DATANASCIMENTO, C.DATACADASTRO, C.IDENDERECO, C.IDTELEFONE, " +
            "  CO.NUMEROCONTA, CO.SALDO, CO.ID AS CO_ID, CO.IDSTATUSCONTA, U.ID AS U_ID, U.CPF, U.SENHA, U.EMAIL, U.IDTIPOUSUARIO " +
            "FROM CLIENTES C " +
            "INNER JOIN USUARIOS U ON C.IDUSUARIO = U.ID " +
            "LEFT JOIN CONTAS CO ON CO.IDCLIENTE = C.ID " +
            "WHERE C.IDUSUARIO = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();

                    // Dados do cliente
                    cliente.setClienteId(rs.getInt("C_ID"));
                    cliente.setNome(rs.getString("NOME"));
                    cliente.setDataNascimento(rs.getDate("DATANASCIMENTO").toLocalDate());
                    cliente.setDataCadastro(rs.getDate("DATACADASTRO").toLocalDate());

                    // Usuario
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("U_ID"));
                    usuario.setCpf(rs.getString("CPF"));
                    usuario.setSenha(rs.getString("SENHA"));
                    usuario.setEmail(rs.getString("EMAIL"));
                    usuario.setTipoUsuario(TipoUsuario.getByCodigo(rs.getInt("IDTIPOUSUARIO")));

                    cliente.setId(usuario.getId());
                    cliente.setCpf(usuario.getCpf());
                    cliente.setEmail(usuario.getEmail());
                    cliente.setTipoUsuario(usuario.getTipoUsuario());

                    // Conta (pode ser null)
                    int coId = rs.getInt("CO_ID");
                    if (!rs.wasNull()) {
                        Conta conta = new Conta();
                        conta.setContaId(coId);
                        conta.setNumeroConta(rs.getString("NUMEROCONTA"));
                        conta.setSaldo(rs.getBigDecimal("SALDO"));
                        conta.setStatusConta(StatusConta.getByCodigo(rs.getInt("IDSTATUSCONTA")));
                        cliente.setConta(conta);
                    }

                    // Endereco e Telefone via DAOs
                    int idEndereco = rs.getInt("IDENDERECO");
                    int idTelefone = rs.getInt("IDTELEFONE");
                    Endereco endereco = enderecoDAO.buscarEnderecoPorId(idEndereco);
                    Telefone telefone = telefoneDAO.buscarTelefonePorId(idTelefone);
                    cliente.setEndereco(endereco);
                    cliente.setTelefone(telefone);

                    return cliente;
                }
            }
        }
        return null;
    }

    public boolean atualizarCliente(Connection conn, Cliente cliente) throws SQLException {
        String sql = "UPDATE Clientes SET nome = ?, dataNascimento = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            if (cliente.getDataNascimento() != null) {
                stmt.setDate(2, java.sql.Date.valueOf(cliente.getDataNascimento()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            // aqui usamos ClienteId (id da tabela Clientes)
            stmt.setInt(3, cliente.getClienteId());
            return stmt.executeUpdate() > 0;
        }
    }

    /* Mantive seus métodos list/others inalterados */
    public List<Cliente> listarClientesPorStatus(int statusCodigo, Connection conn) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql =
            "SELECT U.id AS U_ID, U.CPF, U.SENHA, U.EMAIL, U.IDTIPOUSUARIO, " +
            "C.ID AS C_ID, C.NOME, C.DATANASCIMENTO, C.DATACADASTRO, " +
            "CO.NUMEROCONTA, CO.SALDO, CO.ID AS CO_ID, CO.IDSTATUSCONTA " +
            "FROM CLIENTES C " +
            "INNER JOIN USUARIOS U ON C.idUsuario = U.id " +
            "INNER JOIN CONTAS CO ON CO.idCliente = C.ID " +
            "WHERE CO.IDSTATUSCONTA = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, statusCodigo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("U_ID"));
                    usuario.setCpf(rs.getString("CPF"));
                    usuario.setSenha(rs.getString("SENHA"));
                    usuario.setEmail(rs.getString("EMAIL"));
                    usuario.setTipoUsuario(TipoUsuario.getByCodigo(rs.getInt("IDTIPOUSUARIO")));

                    Conta conta = new Conta();
                    conta.setContaId(rs.getInt("CO_ID"));
                    conta.setNumeroConta(rs.getString("NUMEROCONTA"));
                    conta.setSaldo(rs.getBigDecimal("SALDO"));
                    conta.setStatusConta(StatusConta.getByCodigo(rs.getInt("IDSTATUSCONTA")));

                    Cliente cliente = new Cliente();
                    cliente.setClienteId(rs.getInt("C_ID"));
                    cliente.setNome(rs.getString("NOME"));
                    cliente.setDataNascimento(rs.getDate("DATANASCIMENTO").toLocalDate());
                    cliente.setDataCadastro(rs.getDate("DATACADASTRO").toLocalDate());
                    cliente.setConta(conta);
                    cliente.setId(usuario.getId());
                    cliente.setCpf(usuario.getCpf());
                    cliente.setEmail(usuario.getEmail());
                    cliente.setTipoUsuario(usuario.getTipoUsuario());
                    clientes.add(cliente);
                }
            }
        }
        return clientes;
    }
}
