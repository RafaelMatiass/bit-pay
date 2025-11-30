
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Recuperar Senha</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="col-md-5 mx-auto">
        <div class="card shadow-sm">
            <div class="card-header text-center bg-primary text-white">
                <h4>Recuperar Senha</h4>
            </div>

            <div class="card-body">
                <% if (request.getAttribute("erro") != null) { %>
                    <div class="alert alert-danger">
                        <%= request.getAttribute("erro") %>
                    </div>
                <% } %>

                <form action="<%= request.getContextPath() %>/recuperar-senha" method="post">

                    <div class="mb-3">
                        <label for="email" class="form-label">Digite seu e-mail:</label>
                        <input type="email" id="email" name="email" required class="form-control">
                    </div>

                    <button type="submit" class="btn btn-primary w-100">
                        Enviar código
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
