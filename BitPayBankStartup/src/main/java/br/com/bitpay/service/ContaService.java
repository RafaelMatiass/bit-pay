package br.com.bitpay.service;

import br.com.bitpay.model.Conta;
import java.time.LocalDate;

public interface ContaService {
    
    void abrirConta(String cpf, String email, String senha, String nome, LocalDate dataNascimento,
                    String telefoneStr, String cep, String logradouro, String numeroStr, 
                    String bairro, String cidade, String estado) throws Exception;
    
    Conta buscarContaAtualizada(int idConta) throws Exception;
}