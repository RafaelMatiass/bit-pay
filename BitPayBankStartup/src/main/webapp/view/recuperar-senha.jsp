<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>BitPay - Recuperar Senha</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
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

        .btn-primary{
            background:var(--primary);
            border:none;
            border-radius:14px;
            padding:12px;
            font-weight:600;
            box-shadow:0 8px 20px #0d6efd40;
        }

        .btn-primary:hover{
            transform:translateY(-2px);
            box-shadow:0 12px 26px #0d6efd60;
        }
    </style>
</head>

<body>

<div class="container" style="max-width:480px;">

    <div class="text-center mb-3">
        <span class="px-3 py-1 rounded-pill" style="background:#0d6efd12; color:#0d6efd; font-weight:600; font-size:13px;">
            Recuperação de acesso
        </span>
    </div>

    <div class="card card-modern p-4">

        <h3 class="text-center mb-2" style="color:#0a2a57; font-weight:800;">Recuperar Senha</h3>
        <p class="text-center text-muted mb-4">Digite seu e-mail para enviarmos um código de redefinição.</p>

        <% if (request.getAttribute("erro") != null) { %>
            <div class="alert alert-danger text-center">
                <%= request.getAttribute("erro") %>
            </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/recuperar-senha" method="post">

            <label class="form-label mt-2">E-mail cadastrado:</label>
            <input type="email" name="email" class="form-control" required>

            <button class="btn btn-primary w-100 mt-4">Enviar código</button>

        </form>

        <p class="text-center mt-3">
            <a href="index.jsp" class="text-decoration-none" style="color:#0d6efd;">Voltar ao início</a>
        </p>

    </div>
</div>

</body>
</html>
