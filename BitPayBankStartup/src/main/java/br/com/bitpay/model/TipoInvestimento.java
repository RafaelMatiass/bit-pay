package br.com.bitpay.model;

import java.math.BigDecimal;

public class TipoInvestimento {
	private int id;
	private String nome;
	private BigDecimal rentabilidadeMes;
	private int carenciaDias;
	private BigDecimal valorMinimo;
	
	
	public TipoInvestimento(int id, String nome, BigDecimal rentabilidadeMes, int carenciaDias, BigDecimal valorMinimo) {
		
		this.id = id;
		this.nome = nome;
		this.rentabilidadeMes = rentabilidadeMes;
		this.carenciaDias = carenciaDias;
		this.valorMinimo = valorMinimo;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public BigDecimal getRentabilidadeMes() {
		return rentabilidadeMes;
	}


	public void setRentabilidadeMes(BigDecimal rentabilidadeMes) {
		this.rentabilidadeMes = rentabilidadeMes;
	}


	public int getCarenciaDias() {
		return carenciaDias;
	}


	public void setCarenciaDias(int carenciaDias) {
		this.carenciaDias = carenciaDias;
	}


	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}


	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}
	
}
