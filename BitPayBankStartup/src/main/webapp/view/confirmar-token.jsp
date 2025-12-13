<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>BitPay - Confirmar Token</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        :root {
            --primary:#0d6efd;
            --bg:#f5f7fc;
            --text:#1c1f27;
            --radius:22px;
        }

        body{
            background:radial-gradient(circle at top left,#4c8dff33,#0d6efd11 40%,#0a1a33 100%), var(--bg);
            min-height:100vh;
            display:flex;
            justify-content:center;
            align-items:center;
            padding:20px;
            font-family:Inter, sans-serif;
        }

        .card-modern{
            border-radius:var(--radius);
            border:1px solid #0d6efd25;
            background:linear-gradient(135deg,#ffffff,#eff4ff);
            box-shadow:0 18px 60px #0d6efd33;
        }

        .form-control{
            border-radius:14px;
            padding:10px 12px;
            border:1px solid #cfd4e0;
        }

        .form-control:focus{
            border-color:var(--primary);
            box-shadow:0 0 0 .15rem #0d6efd40;
        }

        .btn-success{
            background:#0d6efd;
            border:none;
            border-radius:14px;
            padding:12px;
            font-weight:600;
            box-shadow:0 8px 20px #0d6efd40;
        }

        .btn-success:hover{
            transform:translateY(-2px);
            box-shadow:0 12px 26px #0d6efd60;
        }
    </style>
</head>

<body>

<div class="container" style="max-width:480px;">

    <div class="text-center mb-3">
        <span class="px-3 py-1 rounded-pill" style="background:#0d6efd12; color:#0d6efd; font-weight:600; font-size:13px;">
            Segurança de conta
        </span>
    </div>

    <% if (request.getAttribute("msg") != null) { %>
        <div class="alert alert-success text-center mb-3">
            <%= request.getAttribute("msg") %>
        </div>
    <% } %>

    <div class="card card-modern p-4">

        <h3 class="text-center mb-2" style="color:#0a2a57; font-weight:800;">Confirmar Código</h3>
        <p class="text-center text-muted mb-4">Informe o código recebido e redefina sua senha.</p>

        <% if (request.getAttribute("erro") != null) { %>
            <div class="alert alert-danger text-center">
                <%= request.getAttribute("erro") %>
            </div>
        <% } %>

        <form action="confirmar-token" method="post">

            <label class="form-label">Código enviado ao e-mail:</label>
            <input type="text" name="codigo" class="form-control" required>

            <label class="form-label mt-3">Nova senha:</label>
            <input type="password" name="novaSenha" class="form-control" required>

            <button class="btn btn-success w-100 mt-4">Alterar Senha</button>

        </form>

        <p class="text-center mt-3">
            <a href="index.jsp" class="text-decoration-none" style="color:#0d6efd;">Voltar ao início</a>
        </p>

    </div>

</div>

</body>
</html>
