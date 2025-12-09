<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Abrir Conta</title>
    <base href="<%= request.getContextPath() %>/">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 mt-5">
                <div class="card">
                    <div class="card-body">
                        <h3 class="card-title text-center">Abra sua Conta Bit Pay</h3>
                        <p class="text-center mb-4">Seus dados serão enviados para análise de crédito.</p>
                        
                        <!-- Formulário de Cadastro -->
                        <!-- O action "cadastro" chamará nosso Servlet -->
                        <form action="cadastro" method="POST">
                            
                            <h5 class="mt-4">Dados de Acesso (Login)</h5>
                            <hr>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="cpf" class="form-label">CPF (Apenas números)</label>
                                    <input type="text" class="form-control" id="cpf" name="cpf" maxlength="11" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="email" class="form-label">E-mail</label>
                                    <input type="email" class="form-control" id="email" name="email" required>
                                </div>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="senha" class="form-label">Crie uma Senha</label>
                                    <input type="password" class="form-control" id="senha" name="senha" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="confirmarSenha" class="form-label">Confirme a Senha</label>
                                    <input type="password" class="form-control" id="confirmarSenha" name="confirmarSenha" required>
                                </div>
                            </div>
                            
                            <h5 class="mt-5">Dados Pessoais</h5>
                            <hr>
                            <div class="row mb-3">
                                <div class="col-md-8">
                                    <label for="nome" class="form-label">Nome Completo</label>
                                    <input type="text" class="form-control" id="nome" name="nome" required>
                                </div>
                                <div class="col-md-4">
                                    <label for="dataNascimento" class="form-label">Data de Nascimento</label>
                                    <input type="date" class="form-control" id="dataNascimento" name="dataNascimento" required>
                                </div>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="telefone" class="form-label">Telefone</label>
                                    <input type="text" class="form-control" id="telefone" name="telefone" required>
                                </div>
                                <!-- Bloco de Endereço Detalhado -->
                                <div class="col-md-6">
                                    <label for="cep" class="form-label">CEP (Apenas números)</label>
                                    <input type="text" class="form-control" id="cep" name="cep" maxlength="8" required>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-9">
                                    <label for="logradouro" class="form-label">Logradouro (Rua, Av.)</label>
                                    <input type="text" class="form-control" id="logradouro" name="logradouro" required>
                                </div>
                                <div class="col-md-3">
                                    <label for="numero" class="form-label">Número</label>
                                    <input type="text" class="form-control" id="numero" name="numero" required>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-4">
                                    <label for="bairro" class="form-label">Bairro</label>
                                    <input type="text" class="form-control" id="bairro" name="bairro" required>
                                </div>
                                <div class="col-md-4">
                                    <label for="cidade" class="form-label">Cidade</label>
                                    <input type="text" class="form-control" id="cidade" name="cidade" required>
                                </div>
                                <div class="col-md-4">
                                    <label for="estado" class="form-label">Estado (UF)</label>
                                    <input type="text" class="form-control" id="estado" name="estado" maxlength="2" required>
                                </div>
                            </div>
                            <!-- Fim do Bloco de Endereço Detalhado -->

                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-success btn-lg">Abrir Minha Conta</button>
                                <a href="index.jsp" class="btn btn-outline-secondary">Voltar para o Login</a>
                            </div>
                        </form>
                        
                        <!-- Mensagem de feedback (sucesso/erro) -->
                        <% 
                            String mensagem = (String) request.getAttribute("mensagem");
                            String tipo = (String) request.getAttribute("tipoMensagem");
                            if (mensagem != null) { 
                        %>
                            <div class="alert alert-<%= tipo %> mt-3" role="alert">
                                <%= mensagem %>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>