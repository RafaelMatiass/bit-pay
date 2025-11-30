package br.com.bitpay.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.bitpay.model.Enums.StatusConta;

public class Conta {
	
	private int ContaId;
	private String NumeroConta;
	private BigDecimal Saldo;
	private LocalDate DataAbertura;
	private StatusConta statusConta;
	private Cliente cliente;
	
	public Conta() {
	}
	
	public Conta(int id, String numeroConta, BigDecimal saldo,  LocalDate dataAbertura, StatusConta statusConta, Cliente cliente) {
		ContaId = id;
		NumeroConta = numeroConta;
		Saldo = saldo;
		DataAbertura = dataAbertura;
		this.statusConta = statusConta;
		this.cliente = cliente;
	}
	
	public Conta(String numeroConta, BigDecimal saldo,  LocalDate dataAbertura, StatusConta statusConta, Cliente cliente) {
		NumeroConta = numeroConta;
		Saldo = saldo;
		DataAbertura = dataAbertura;
		this.statusConta = statusConta;
		
		this.cliente = cliente;
	}

	public int getContaId() {
		return ContaId;
	}

	public void setContaId(int contaId) {
		ContaId = contaId;
	}


	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNumeroConta() {
		return NumeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		NumeroConta = numeroConta;
	}

	public BigDecimal getSaldo() {
		return Saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		Saldo = saldo;
	}

	public  LocalDate getDataAbertura() {
		return DataAbertura;
	}

	public void setDataAbertura( LocalDate dataAbertura) {
		DataAbertura = dataAbertura;
	}

	public StatusConta getStatusConta() {
		return statusConta;
	}

	public void setStatusConta(StatusConta statusConta) {
		this.statusConta = statusConta;
	}
	
	
	
}
