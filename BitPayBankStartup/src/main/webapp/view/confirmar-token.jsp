<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Confirmar Token</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="col-md-5 mx-auto">

        <% if (request.getAttribute("msg") != null) { %>
            <div class="alert alert-success text-center">
                <%= request.getAttribute("msg") %>
            </div>
        <% } %>

        <div class="card shadow-sm">
            <div class="card-header bg-success text-white text-center">
                <h4>Confirmar Código</h4>
            </div>

            <div class="card-body">
                <% if (request.getAttribute("erro") != null) { %>
                    <div class="alert alert-danger">
                        <%= request.getAttribute("erro") %>
                    </div>
                <% } %>

                <form action="confirmar-token" method="post">
                    <div class="mb-3">
                        <label class="form-label">Token recebido no e-mail:</label>
                        <input type="text" name="codigo" required class="form-control">

                    </div>

                    <div class="mb-3">
                        <label class="form-label">Nova senha:</label>
                        <input type="password" name="novaSenha" required class="form-control">
                    </div>

                    <button type="submit" class="btn btn-success w-100">
                        Alterar Senha
                    </button>
                </form>
            </div>
        </div>

    </div>
</div>

</body>
</html>