<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Login</title>
    <base href="<%= request.getContextPath() %>/">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5 mt-5">
                <div class="card">
                    <div class="card-body">
                        <h3 class="card-title text-center">Bit Pay Bank</h3>
                        <p class="text-center">Acesse sua conta</p>
                        
                        <form action="login" method="POST">
                            <div class="mb-3">
                                <label for="cpf" class="form-label">CPF</label>
                                <input type="text" class="form-control" id="cpf" name="cpf" required>
                            </div>
                            <div class="mb-3">
                                <label for="senha" class="form-label">Senha</label>
                                <input type="password" class="form-control" id="senha" name="senha" required>
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">Entrar</button>
                            </div>
                           
                        </form>
                        
                            <p class="text-center">
                           <!-- Link de Cadastro (Teste 2) -->
                           <!-- Chamando o Servlet, não o JSP! -->
                            Ainda não é cliente? <a href="cadastro-page">Quero abrir uma conta</a>
                           </p>
                        
                        <% 
                            String erro = (String) request.getAttribute("erro");
                            if (erro != null) { 
                        %>
                            <div class="alert alert-danger mt-3" role="alert">
                                <%= erro %>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>