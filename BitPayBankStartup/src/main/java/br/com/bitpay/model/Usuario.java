package br.com.bitpay.model;

import br.com.bitpay.model.Enums.TipoUsuario;

public class Usuario {
	
	private int id;
	private String CPF;  
	private String Senha;
	private String Email;
	private TipoUsuario tipoUsuario;
	
	public Usuario(String cpf, String senha, String email, TipoUsuario tipoUsuario) {
		CPF = cpf;
		Senha = senha;
		Email = email;
		this.tipoUsuario = tipoUsuario;
	}
	
	public Usuario(int id, String cpf, String senha, String email, TipoUsuario tipoUsuario) {
		this.id = id;
		CPF = cpf;
		Senha = senha;
		Email = email;
		this.tipoUsuario = tipoUsuario;		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public String getSenha() {
		return Senha;
	}

	public void setSenha(String senha) {
		Senha = senha;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
	
	
	
}
