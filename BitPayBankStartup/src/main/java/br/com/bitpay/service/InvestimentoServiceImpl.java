package br.com.bitpay.service;

import br.com.bitpay.dao.ContaDAO;
import br.com.bitpay.dao.InvestimentoDAO;
import br.com.bitpay.dao.MovimentacaoDAO;
import br.com.bitpay.model.AplicacaoInvestimento;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.Movimentacao;
import br.com.bitpay.model.TipoInvestimento;
import br.com.bitpay.model.Enums.TipoMovimento;
import br.com.bitpay.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.List;

public class InvestimentoServiceImpl implements InvestimentoService {

    private final InvestimentoDAO investimentoDAO = new InvestimentoDAO();
    private final ContaDAO contaDAO = new ContaDAO();

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

    
            
             movimentacaoDAO.inserir(conn, new Movimentacao(valor,idConta, null, TipoMovimento.INVESTIMENTO));
            
        
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
}