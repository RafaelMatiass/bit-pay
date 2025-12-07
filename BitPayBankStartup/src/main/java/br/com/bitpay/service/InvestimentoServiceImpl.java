package br.com.bitpay.service;

import br.com.bitpay.dao.ClienteDAO;
import br.com.bitpay.dao.ContaDAO;
import br.com.bitpay.dao.InvestimentoDAO;
import br.com.bitpay.dao.MovimentacaoDAO;
import br.com.bitpay.model.AplicacaoInvestimento;
import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.Movimentacao;
import br.com.bitpay.model.TipoInvestimento;
import br.com.bitpay.model.Enums.TipoMovimento;
import br.com.bitpay.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class InvestimentoServiceImpl implements InvestimentoService {

    private final InvestimentoDAO investimentoDAO = new InvestimentoDAO();
    private final ContaDAO contaDAO = new ContaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO(); 

    @Override
    public List<TipoInvestimento> listarTiposInvestimento() throws Exception {
      
        return investimentoDAO.listarTiposInvestimento();
    }

    @Override
    public void aplicarInvestimento(int idConta, int idTipoInvestimento, BigDecimal valor) throws Exception {
        
     
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da aplicação deve ser positivo.");
        }
        
        Connection conn = null;
        try {
         
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

       
            TipoInvestimento tipo = investimentoDAO.buscarTipoInvestimentoPorId(idTipoInvestimento);
            
            if (tipo == null) {
                throw new IllegalArgumentException("Tipo de investimento não encontrado.");
            }
            
            
            Conta conta = new Conta(); 
            
            conta = contaDAO.buscarContaPorId(idConta); 
             if (conta == null) { throw new IllegalArgumentException("Conta não encontrada."); }
            
         
            
            if (valor.compareTo(tipo.getValorMinimo()) < 0) {
                throw new IllegalArgumentException("Valor mínimo de aplicação não atingido: R$ " + tipo.getValorMinimo());
            }
            
            if (valor.compareTo(conta.getSaldo()) > 0) { 
                throw new IllegalArgumentException("Saldo insuficiente na conta corrente.");
            }

    
            
             movimentacaoDAO.inserir(conn, new Movimentacao(valor.negate(),idConta, null, TipoMovimento.INVESTIMENTO));
            
        
            AplicacaoInvestimento aplicacao = new AplicacaoInvestimento(idConta, tipo, valor);
            investimentoDAO.aplicarInvestimento(conn, aplicacao);
            
          
            conn.commit();
            
        } catch (SQLException e) {
          
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException rollbackEx) {
                    System.err.println("Erro durante o rollback: " + rollbackEx.getMessage());
                }
            }
            throw new Exception("Erro de persistência na aplicação de investimento: " + e.getMessage(), e);
        } finally {
           
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
                }
            }
        }
    }
    
    @Override
    public Conta buscarContaPorIdUsuario(int idUsuario) throws Exception {
    	
    	try {
    		Connection conn = ConnectionFactory.getConnection();
            Cliente cliente = clienteDAO.buscarClientePorIdUsuario(idUsuario, conn);
           
            
            if (cliente == null) {
                 throw new IllegalArgumentException("Cliente não encontrado para o usuário logado.");
            }
            
            Conta conta = cliente.getConta(); 
            
            if (conta == null) {
                throw new IllegalArgumentException("Conta bancária não encontrada para o cliente.");
            }
            
            return conta;
    		
    	}catch(Exception ex) {
    		throw new Exception(ex.getMessage());
    	}
    	
    }
    
    public List<AplicacaoInvestimento> listarPortfolio(int idConta) throws Exception {
        return investimentoDAO.listarAplicacoesPorConta(idConta);
    }
    
    private BigDecimal calcularValorResgate(AplicacaoInvestimento aplicacao) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataAplicacao = aplicacao.getDataAplicacao();
        
       
        long mesesPassados = ChronoUnit.MONTHS.between(dataAplicacao, hoje);
        
        if (mesesPassados < 0) return aplicacao.getValorAplicado(); 

        BigDecimal valor = aplicacao.getValorAplicado();
        BigDecimal taxaMensal = aplicacao.getTipoInvestimento().getRentabilidadeMes();
        
      
        for (int i = 0; i < mesesPassados; i++) {
           
            BigDecimal rendimento = valor.multiply(taxaMensal).setScale(2, RoundingMode.HALF_UP);
            valor = valor.add(rendimento).setScale(2, RoundingMode.HALF_UP);
        }
        
        return valor;
    }
    
    @Override
    public BigDecimal resgatarInvestimento(int idAplicacao, int idConta) throws Exception {
        
        Connection conn = null;
        try {
           
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            
            
            AplicacaoInvestimento aplicacao = investimentoDAO.buscarAplicacaoPorId(conn, idAplicacao);
            
            if (aplicacao == null || aplicacao.getIdConta() != idConta) {
                throw new IllegalArgumentException("Aplicação não encontrada ou não pertence a esta conta.");
            }
            if (aplicacao.getStatus().equals("RESGATADA")) {
                throw new IllegalArgumentException("Esta aplicação já foi resgatada.");
            }
            
           
            LocalDate hoje = LocalDate.now();
            LocalDate dataLiberacao = aplicacao.getDataAplicacao().plusDays(aplicacao.getTipoInvestimento().getCarenciaDias());
            
            if (hoje.isBefore(dataLiberacao)) {
                String dataFormatada = dataLiberacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                throw new IllegalArgumentException("Carência não cumprida. Resgate disponível a partir de " + dataFormatada + ".");
            }
            
          
            BigDecimal valorResgate = calcularValorResgate(aplicacao);
            
           
            investimentoDAO.resgatarAplicacao(conn, idAplicacao); 
            
        
             movimentacaoDAO.inserir(conn, new Movimentacao(valorResgate,idConta, null, TipoMovimento.RESGATE));

          
            conn.commit();
            
            return valorResgate;
            
        } catch (SQLException e) {
          
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException rollbackEx) {
                    System.err.println("Erro durante o rollback: " + rollbackEx.getMessage());
                }
            }
            throw new Exception("Erro de persistência no resgate: " + e.getMessage(), e);
        } finally {
      
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
                }
            }
        }
    }
    
    
}