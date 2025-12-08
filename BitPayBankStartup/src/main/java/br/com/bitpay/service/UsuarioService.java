package br.com.bitpay.service;

import br.com.bitpay.model.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha) throws Exception;

    boolean usuarioExiste(String cpf, String email) throws Exception;

    Usuario buscarPorId(int id) throws Exception;

    void atualizar(Usuario usuario) throws Exception;   // ADICIONADO
}
