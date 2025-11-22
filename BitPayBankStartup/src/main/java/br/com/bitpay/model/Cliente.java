package br.com.bitpay.model;


import java.time.LocalDate;

import br.com.bitpay.model.Enums.TipoUsuario;

public class Cliente extends Usuario {
	private int ClienteId;
	private String Nome;
	private LocalDate dataNascimento;
	private LocalDate dataCadastro;
	private Endereco endereco;
	private Telefone telefone;
	
	
	public Cliente(int idUsuario, String cpf,
			String senha, 
			String email, 
			TipoUsuario tipoUsuario,
			String nome, 
			LocalDate dataNascimento,
			LocalDate dataCadastro, 
			Endereco endereco, 
			Telefone telefone 
			) {
		super(idUsuario, cpf, senha,email,tipoUsuario);
		this.Nome = nome;
		this.dataNascimento = dataNascimento;
		this.dataCadastro = dataCadastro;
		this.endereco = endereco;
		this.telefone = telefone;
		
	}

	

	public int getClienteId() {
		return ClienteId;
	}


	public void setClienteId(int clienteId) {
		ClienteId = clienteId;
	}


	public String getNome() {
		return Nome;
	}


	public void setNome(String nome) {
		Nome = nome;
	}


	public LocalDate getDataNascimento() {
		return dataNascimento;
	}


	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


	public Endereco getEndereco() {
		return endereco;
	}


	public void setEnderecoId(Endereco endereco) {
		this.endereco = endereco;
	}


	public LocalDate getDataCadastro() {
		return dataCadastro;
	}



	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}



	public Telefone getTelefone() {
		return telefone;
	}


	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	
}
