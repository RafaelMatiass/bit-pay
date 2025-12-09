package br.com.bitpay.service;

import br.com.bitpay.dao.EmprestimoDAO;
import br.com.bitpay.dao.MovimentacaoDAO;
import br.com.bitpay.dao.ParcelaEmprestimoDAO;
import br.com.bitpay.model.Emprestimo;
import br.com.bitpay.model.Movimentacao;
import br.com.bitpay.model.ParcelaEmprestimo;
import br.com.bitpay.model.Enums.StatusParcelasEmprestimo;
import br.com.bitpay.model.Enums.TipoMovimento;
import br.com.bitpay.util.ConnectionFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoService {

    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO(); 
    
    private static final TipoMovimento TIPO_CREDITO_EMPRESTIMO = TipoMovimento.EMPRESTIMO;
    
    private final ParcelaEmprestimoDAO parcelaDAO = new ParcelaEmprestimoDAO();
    
    private static final int SCALE = 6; 
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP; 
    
    private static final BigDecimal CEM = new BigDecimal("100");

    public List<ParcelaEmprestimo> simularEmprestimo(BigDecimal valorTotal, BigDecimal valorEntrada, BigDecimal taxaJurosAnual, int numeroParcelas, LocalDate dataInicio) {
        
        BigDecimal saldoDevedorInicial = valorTotal.subtract(valorEntrada);
        BigDecimal taxaDecimalMensal = taxaJurosAnual
                                            .divide(CEM, SCALE, ROUNDING_MODE);
        BigDecimal amortizacaoBase = saldoDevedorInicial.divide(BigDecimal.valueOf(numeroParcelas), SCALE, ROUNDING_MODE)
                                                        .setScale(2, ROUNDING_MODE);
        BigDecimal saldoDevedor = saldoDevedorInicial;
        LocalDate dataVencimento = dataInicio.plusMonths(1);
        List<ParcelaEmprestimo> parcelas = new ArrayList<>();

        for (int i = 1; i <= numeroParcelas; i++) {
            
            BigDecimal juros = saldoDevedor.multiply(taxaDecimalMensal).setScale(2, ROUNDING_MODE);
            BigDecimal amortizacao;
            if (i == numeroParcelas) {
                amortizacao = saldoDevedor.setScale(2, ROUNDING_MODE); 
            } else {
                amortizacao = amortizacaoBase;
            }

            BigDecimal valorTotalParcela = amortizacao.add(juros);
            saldoDevedor = saldoDevedor.subtract(amortizacao);
            
            if (i == numeroParcelas) {
                saldoDevedor = BigDecimal.ZERO.setScale(2, ROUNDING_MODE);
            }

            ParcelaEmprestimo parcela = new ParcelaEmprestimo();
            parcela.setNumeroParcela(i);
            parcela.setValorParcela(valorTotalParcela);
            parcela.setValorAmortizacao(amortizacao);
            parcela.setValorJuros(juros);
            parcela.setSaldoDevedorAtual(saldoDevedor);
            parcela.setDataVencimento(dataVencimento);
            parcela.setStatus(StatusParcelasEmprestimo.ABERTA);
            
            parcelas.add(parcela);
            dataVencimento = dataVencimento.plusMonths(1);
        }

        return parcelas;
    }
    
    public void contratarEmprestimo(Emprestimo emprestimo) throws Exception {
        
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); 
                        
            BigDecimal saldoDevedorEfetivo = emprestimo.getValorTotal().subtract(emprestimo.getValorEntrada());
            emprestimo.setValorTotal(saldoDevedorEfetivo);
            
            emprestimoDAO.inserir(conn, emprestimo);
            parcelaDAO.inserirTodas(conn, emprestimo.getParcelas(), emprestimo.getId());
            
            BigDecimal valorACreditar = emprestimo.getValorTotal().subtract(emprestimo.getValorEntrada());
            int idConta = emprestimo.getIdConta();
                                    
            Movimentacao movCredito = new Movimentacao();
            movCredito.setValor(valorACreditar);
            movCredito.setDataMovimento(LocalDate.now());
            movCredito.setIdConta(idConta);
            movCredito.setIdContaDestino(null); 
            movCredito.setTipoMovimento(TIPO_CREDITO_EMPRESTIMO);
            
            movimentacaoDAO.inserir(conn, movCredito);
            
            conn.commit(); 
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Erro ao tentar fazer rollback: " + ex.getMessage());
                }
            }
            throw new Exception("Falha na contratação do empréstimo. Transação desfeita.", e);
        } finally {
            conn.close();
        }
    }
}