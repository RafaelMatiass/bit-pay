package br.com.bitpay.model;

import java.time.LocalDate;
import br.com.bitpay.model.Enums.TipoUsuario;

public class Cliente extends Usuario {
    
    private Conta conta;
    private int ClienteId;
    private String Nome;
    private LocalDate dataNascimento; 
    private LocalDate dataCadastro; 
    private Endereco endereco;
    private Telefone telefone;
    
    public Cliente() {
        super(); 
    }
    
    public Cliente(int id,String cpf,
            String senha, 
            String email, 
            TipoUsuario tipoUsuario,
            String nome, 
            LocalDate dataNascimento,
            LocalDate dataCadastro, 
            Endereco endereco, 
            Telefone telefone
            ) {
        super(id,cpf, senha, email, tipoUsuario);
        this.Nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
        this.endereco = endereco;
        this.telefone = telefone;
    }
    
    public Cliente(String cpf,
            String senha, 
            String email, 
            TipoUsuario tipoUsuario,
            String nome, 
            LocalDate dataNascimento,
            LocalDate dataCadastro, 
            Endereco endereco, 
            Telefone telefone
            ) {
        super(cpf, senha, email, tipoUsuario);
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
        this.ClienteId = clienteId;
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

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }
    
    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }
}