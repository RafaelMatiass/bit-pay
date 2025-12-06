package br.com.bitpay.model;

import java.time.LocalDate;

public class Gerente extends Usuario {

    private String nome;
    private LocalDate dataNascimento;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}