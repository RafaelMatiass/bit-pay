<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Conta"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.text.NumberFormat, java.util.Locale"%>

<%
    Conta conta = (Conta) session.getAttribute("conta");

    if (conta == null) {
        response.sendRedirect("login");
        return;
    }

    BigDecimal saldo = conta.getSaldo();
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt","BR"));
    String saldoAtual = nf.format(saldo);
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>BitPay • Depósito</title>
    <base href="<%= request.getContextPath() %>/">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        :root {
            --primary:#0d6efd;
            --primary-light:#e9f1ff;
            --bg:#f5f7fc;
            --text:#1c1f27;
            --radius:22px;
        }

        body {
            background: radial-gradient(circle at top left,#4c8dff33,#0d6efd11 40%,#0a1a33 100%), var(--bg);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 22px;
            font-family: Inter, sans-serif;
        }

        .card-modern {
            background: linear-gradient(135deg, #ffffff, #eff4ff);
            border-radius: var(--radius);
            padding: 28px;
            border: 1px solid #0d6efd25;
            box-shadow: 0 18px 50px #0d6efd33;
        }

        .card-modern h3 {
            font-weight: 800;
            color: #0a2a57;
        }

        .form-control {
            border-radius: 14px;
            padding: 10px 12px;
            border: 1px solid #cfd4e0;
        }

        .form-control:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 .15rem #0d6efd40;
        }

        .btn-primary {
            background: var(--primary);
            border: none;
            border-radius: 14px;
            padding: 12px;
            font-weight: 600;
            box-shadow: 0 10px 26px #0d6efd55;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 14px 34px #0d6efd50;
        }

        .btn-secondary {
            border-radius: 14px;
            padding: 12px;
            font-weight: 600;
        }

        footer {
            text-align: center;
            margin-top: 22px;
            opacity: .55;
            font-size: 13px;
        }
    </style>
</head>

<body>

<div class="container" style="max-width: 480px;">

    <div class="text-center mb-3">
        <span class="px-3 py-1 rounded-pill" style="background:#0d6efd12; color:#0d6efd; font-weight:600;">
            Área de Depósitos
        </span>
    </div>

    <div class="card-modern">

        <h3 class="text-center mb-2">Realizar Depósito</h3>
        <p class="text-center text-muted mb-4">
            Saldo atual: <strong><%= saldoAtual %></strong><br>
            Conta <%= conta.getNumeroConta() %>
        </p>

        <!-- Formulário -->
        <form action="deposito" method="POST">

            <label for="valor" class="form-label">Valor do depósito</label>
            <input 
                type="number" 
                step="0.01" 
                min="0.01" 
                class="form-control" 
                id="valor" 
                name="valor" 
                placeholder="R$ 0,00"
                required>

            <button type="submit" class="btn btn-primary w-100 mt-4">
                Depositar
            </button>

            <a href="home" class="btn btn-secondary w-100 mt-3">
                Voltar
            </a>
        </form>

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

    <footer>
        BitPay Blue • Segurança, tecnologia e futuro financeiro.
    </footer>

</div>

</body>
</html>
