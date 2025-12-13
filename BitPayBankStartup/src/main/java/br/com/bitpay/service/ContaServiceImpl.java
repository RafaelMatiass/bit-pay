package br.com.bitpay.service;

import br.com.bitpay.dao.ClienteDAO;
import br.com.bitpay.dao.ContaDAO;
import br.com.bitpay.dao.EnderecoDAO;
import br.com.bitpay.dao.TelefoneDAO;
import br.com.bitpay.dao.UsuarioDAO;
import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.Endereco;
import br.com.bitpay.model.Telefone;
import br.com.bitpay.model.Usuario;
import br.com.bitpay.model.Enums.StatusConta;
import br.com.bitpay.model.Enums.TipoUsuario;
import br.com.bitpay.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ContaServiceImpl implements ContaService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EnderecoDAO enderecoDAO = new EnderecoDAO();
    private final TelefoneDAO telefoneDAO = new TelefoneDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ContaDAO contaDAO = new ContaDAO();

    private static final int COD_PAIS_PADRAO = 55;

    @Override
    public void abrirConta(
            String cpf,
            String email,
            String senha,
            String nome,
            LocalDate dataNascimento,
            String telefoneStr,
            String cep,
            String logradouro,
            String numeroStr,
            String bairro,
            String cidade,
            String estado
    ) throws Exception {

        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            if (usuarioDAO.existeUsuario(conn, cpf, email)) {
                throw new IllegalArgumentException("CPF ou E-mail já cadastrados.");
            }

            int numeroEndereco = Integer.parseInt(numeroStr);
            Endereco novoEndereco = new Endereco(
                logradouro, numeroEndereco, null, bairro, cidade, estado, cep
            );

            String telLimpo = telefoneStr.replaceAll("[^0-9]", "");
            int codArea = Integer.parseInt(telLimpo.substring(0, 2));
            long numeroTel = Long.parseLong(telLimpo.substring(2));
            Telefone novoTelefone = new Telefone(COD_PAIS_PADRAO, codArea, numeroTel);

            Usuario novoUsuario = new Usuario(cpf, senha, email, TipoUsuario.CLIENTE);
            int idUsuario = usuarioDAO.inserir(conn, novoUsuario);
            novoUsuario.setId(idUsuario);

            int idEndereco = enderecoDAO.inserirEndereco(conn, novoEndereco);
            novoEndereco.setId(idEndereco);

            int idTelefone = telefoneDAO.inserirTelefone(conn, novoTelefone);
            novoTelefone.setId(idTelefone);

            Cliente novoCliente = new Cliente(
                cpf, senha, email, TipoUsuario.CLIENTE,
                nome, dataNascimento, LocalDate.now(),
                novoEndereco, novoTelefone
            );

            // FK CLIENTES.IDUSUARIO
            novoCliente.setId(idUsuario);

            // ✅ CAPTURA O ID REAL DO CLIENTE
            int idCliente = clienteDAO.inserir(conn, novoCliente);
            novoCliente.setClienteId(idCliente);

            String numeroConta = "100" + idUsuario + "-1";

            Conta novaConta = new Conta(
                numeroConta,
                BigDecimal.ZERO,
                LocalDate.now(),
                StatusConta.PENDENTE,
                novoCliente
            );

            contaDAO.inserirConta(conn, novaConta);

            conn.commit();

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    public Conta buscarContaAtualizada(int idConta) throws Exception {
        String sql = "SELECT id, numeroConta, saldo, idStatusConta FROM CONTAS WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idConta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Conta conta = new Conta();
                    conta.setContaId(rs.getInt("id"));
                    conta.setNumeroConta(rs.getString("numeroConta"));
                    conta.setSaldo(rs.getBigDecimal("saldo"));
                    conta.setStatusConta(
                        StatusConta.getByCodigo(rs.getInt("idStatusConta"))
                    );
                    return conta;
                }
            }
        }
        throw new Exception("Conta não encontrada.");
    }
}
