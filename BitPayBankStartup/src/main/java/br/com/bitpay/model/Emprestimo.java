package br.com.bitpay.model;

import br.com.bitpay.model.Enums.StatusEmprestimo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Emprestimo {

    private int id;
    private BigDecimal valorTotal; 
    private BigDecimal valorEntrada;
    private LocalDate dataContratacao;
    private int numerosParcelas;
    private BigDecimal taxaJuros; 
    private int idConta; 
    private StatusEmprestimo status; 
    
    private List<ParcelaEmprestimo> parcelas; 

    public Emprestimo() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    
    public BigDecimal getValorEntrada() { return valorEntrada; }
    public void setValorEntrada(BigDecimal valorEntrada) { this.valorEntrada = valorEntrada; }

    public LocalDate getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDate dataContratacao) { this.dataContratacao = dataContratacao; }

    public int getNumerosParcelas() { return numerosParcelas; }
    public void setNumerosParcelas(int numerosParcelas) { this.numerosParcelas = numerosParcelas; }

    public BigDecimal getTaxaJuros() { return taxaJuros; }
    public void setTaxaJuros(BigDecimal taxaJuros) { this.taxaJuros = taxaJuros; }

    public int getIdConta() { return idConta; }
    public void setIdConta(int idConta) { this.idConta = idConta; }

    public StatusEmprestimo getStatus() { return status; }
    public void setStatus(StatusEmprestimo status) { this.status = status; }
    
    public List<ParcelaEmprestimo> getParcelas() { return parcelas; }
    public void setParcelas(List<ParcelaEmprestimo> parcelas) { this.parcelas = parcelas; }
}