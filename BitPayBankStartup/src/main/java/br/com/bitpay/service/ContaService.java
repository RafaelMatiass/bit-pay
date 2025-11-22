package br.com.bitpay.service;

import java.time.LocalDate;


public interface ContaService {
    
    /**
     * Realiza todo o processo de abertura de conta, inserindo dados em 5 tabelas
     * de forma transacional.
     * * @throws Exception Se houver erro de validação ou banco de dados.
     */
    void abrirConta(String cpf, String email, String senha, String nome, LocalDate dataNascimento,
                    String telefoneStr, String cep, String logradouro, String numeroStr, 
                    String bairro, String cidade, String estado) throws Exception;
}