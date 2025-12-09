package br.com.bitpay.model;

import br.com.bitpay.model.Enums.StatusParcelasEmprestimo;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ParcelaEmprestimo {

    private int id;
    private int numeroParcela;
    private BigDecimal valorParcela; 
    private LocalDate dataVencimento;
    private BigDecimal valorPago;
    private LocalDate dataPagamento;
    private int idEmprestimo; 
    private StatusParcelasEmprestimo status;
    
    private BigDecimal valorAmortizacao; 
    private BigDecimal valorJuros;       
    private BigDecimal saldoDevedorAtual; 

    public ParcelaEmprestimo() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumeroParcela() { return numeroParcela; }
    public void setNumeroParcela(int numeroParcela) { this.numeroParcela = numeroParcela; }

    public BigDecimal getValorParcela() { return valorParcela; }
    public void setValorParcela(BigDecimal valorParcela) { this.valorParcela = valorParcela; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public BigDecimal getValorPago() { return valorPago; }
    public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }

    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

    public int getIdEmprestimo() { return idEmprestimo; }
    public void setIdEmprestimo(int idEmprestimo) { this.idEmprestimo = idEmprestimo; }

    public StatusParcelasEmprestimo getStatus() { return status; }
    public void setStatus(StatusParcelasEmprestimo status) { this.status = status; }

    public BigDecimal getValorAmortizacao() { return valorAmortizacao; }
    public void setValorAmortizacao(BigDecimal valorAmortizacao) { this.valorAmortizacao = valorAmortizacao; }

    public BigDecimal getValorJuros() { return valorJuros; }
    public void setValorJuros(BigDecimal valorJuros) { this.valorJuros = valorJuros; }
    
    public BigDecimal getSaldoDevedorAtual() { return saldoDevedorAtual; }
    public void setSaldoDevedorAtual(BigDecimal saldoDevedorAtual) { this.saldoDevedorAtual = saldoDevedorAtual; }
}