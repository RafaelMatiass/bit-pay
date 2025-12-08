<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Login</title>
    <base href="<%= request.getContextPath() %>/">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5 mt-5">

                <div class="card shadow">
                    <div class="card-body">

                        <h3 class="card-title text-center mb-1">Bit Pay Bank</h3>
                        <p class="text-center text-muted mb-4">Acesse sua conta</p>
                        
                        <!-- Formulário -->
                        <form action="login" method="POST">

                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input 
                                    type="email" 
                                    class="form-control" 
                                    id="email" 
                                    name="email" 
                                    required
                                >
                            </div>

                            <div class="mb-2">
                                <label for="senha" class="form-label">Senha</label>
                                <input 
                                    type="password" 
                                    class="form-control" 
                                    id="senha" 
                                    name="senha" 
                                    required
                                >
                            </div>

                            <!-- Esqueci minha senha -->
                            <div class="text-end mb-3">
                                <a href="view/recuperar-senha.jsp" class="small text-primary">
                                    Esqueci minha senha
                                </a>
                            </div>

                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">
                                    Entrar
                                </button>
                            </div>
                        </form>

                        <!-- Abrir conta -->
                        <p class="text-center mt-3">
                            Ainda não é cliente?  
                            <a href="view/cadastro.jsp" class="text-decoration-none">Abrir conta</a>
                        </p>

                        <!-- Mensagens -->
                        <%
                            String mensagem = (String) request.getAttribute("mensagem");
                            String tipo = (String) request.getAttribute("tipoMensagem");
                            if (mensagem != null) {
                        %>
                            <div class="alert alert-<%= tipo %> mt-3">
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
