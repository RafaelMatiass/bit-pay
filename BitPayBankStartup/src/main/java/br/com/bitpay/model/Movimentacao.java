package br.com.bitpay.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.bitpay.model.Enums.TipoMovimento;

public class Movimentacao {
	
	private int id;
	private BigDecimal valor;
	private LocalDate dataMovimento;
	private int idConta; 
	private Integer idContaDestino; 
	private TipoMovimento tipoMovimento;
	private String numeroContaDestino; 
	private String nomeClienteDestino;
	
	
	
	public Movimentacao(BigDecimal valor, int idConta, Integer idContaDestino, TipoMovimento tipoMovimento) {
		
		this.valor = valor;
		this.dataMovimento = LocalDate.now();
		this.idConta = idConta;
		this.idContaDestino = idContaDestino;
		this.tipoMovimento = tipoMovimento;
	}

	public Movimentacao() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(LocalDate dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public int getIdConta() {
		return idConta;
	}

	public void setIdConta(int idConta) {
		this.idConta = idConta;
	}

	public Integer getIdContaDestino() {
		return idContaDestino;
	}

	public void setIdContaDestino(Integer idContaDestino) {
		this.idContaDestino = idContaDestino;
	}

	public TipoMovimento getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(TipoMovimento tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	
	public String getNumeroContaDestino() {
		return numeroContaDestino;
	}

	public void setNumeroContaDestino(String numeroContaDestino) {
		this.numeroContaDestino = numeroContaDestino;
	}

	public String getNomeClienteDestino() {
		return nomeClienteDestino;
	}

	public void setNomeClienteDestino(String nomeClienteDestino) {
		this.nomeClienteDestino = nomeClienteDestino;
	}
}