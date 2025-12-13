<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Cliente" %>
<%@ page import="br.com.bitpay.model.Conta" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat, java.util.Locale" %>

<%
    Object usuarioLogado = session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !(usuarioLogado instanceof Cliente)) {
        response.sendRedirect(request.getContextPath() + "/index.jsp"); 
        return;
    }

    Cliente cliente = (Cliente) usuarioLogado;
    Conta conta = cliente.getConta();

    if (conta == null) {
        response.sendRedirect(request.getContextPath() + "/login"); 
        return;
    }

    String nomeUsuario = cliente.getNome().split(" ")[0];
    BigDecimal saldoConta = conta.getSaldo(); 

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    String saldoFormatado = nf.format(saldoConta);
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>BitPay • Home</title>
    <base href="<%= request.getContextPath() %>/">

    <!-- Bootstrap + Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

    <style>
        :root {
            --primary:#0d6efd;
            --primary-light:#e6efff;
            --bg:#f4f7fc;
            --dark:#0b1b40;
            --radius:22px;
        }

        *{font-family: Inter, sans-serif;}

        body{
            background: linear-gradient(180deg,#f3f7ff 0%,#eef3ff 30%,#e8edff 100%);
            min-height: 100vh;
        }

        /* NAVBAR */
        .navbar-modern{
            backdrop-filter: blur(12px);
            background: #ffffffaa;
            border-bottom:1px solid #d0dbff66;
        }

        .navbar-modern .navbar-brand{
            font-size:26px;
            font-weight:800;
            color:var(--primary);
        }

        .navbar-modern .navbar-text{
            font-size:16px;
            font-weight:600;
            color:var(--dark);
        }

        /* CARD DE SALDO */
        .saldo-card{
            background:linear-gradient(145deg,#0d6efd,#478dff);
            color:white;
            padding:38px;
            border-radius:var(--radius);
            box-shadow:0 18px 40px #0d6efd55;
        }

        .saldo-card small{
            opacity:0.85;
        }

        .saldo-valor{
            font-size:46px;
            font-weight:800;
        }

        /* CARDS DE FUNÇÕES */
        .menu-card{
            background:white;
            padding:26px;
            border-radius:var(--radius);
            box-shadow:0 8px 26px #0d6efd20;
            transition:.25s;
            border:1px solid #d8e0ff66;
        }

        .menu-card:hover{
            transform:translateY(-6px);
            box-shadow:0 14px 36px #0d6efd30;
        }

        .menu-card i{
            font-size:36px;
            color:var(--primary);
            margin-bottom:12px;
        }

        /* TÍTULOS */
        .section-title{
            font-size:22px;
            font-weight:800;
            margin-top:30px;
            color:#0a2a57;
        }
    </style>
</head>

<body>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-modern py-3">
    <div class="container">
        <a class="navbar-brand" href="home">BitPay</a>

        <div class="collapse navbar-collapse justify-content-end">
            <span class="navbar-text me-3">
                Olá, <%= nomeUsuario %>
            </span>

            <a href="logout" class="btn btn-outline-danger rounded-pill px-4">
                Sair
            </a>
        </div>
    </div>
</nav>

<!-- SALDO -->
<div class="container mt-5">
    <div class="saldo-card">
        <small>Saldo disponível</small>
        <div class="saldo-valor"><%= saldoFormatado %></div>
        <small>Conta • <%= conta.getNumeroConta() %></small>
    </div>
</div>

<!-- FUNÇÕES -->
<div class="container">
    <h3 class="section-title">O que você deseja fazer?</h3>

    <div class="row g-4 mt-1">

        <div class="col-md-4">
            <a href="transferencia" class="text-decoration-none text-dark">
                <div class="menu-card text-center">
                    <i class="bi bi-send-fill"></i>
                    <h5 class="mt-2">Transferência</h5>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="deposito" class="text-decoration-none text-dark">
                <div class="menu-card text-center">
                    <i class="bi bi-cash-stack"></i>
                    <h5 class="mt-2">Depósito</h5>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="extrato" class="text-decoration-none text-dark">
                <div class="menu-card text-center">
                    <i class="bi bi-receipt-cutoff"></i>
                    <h5 class="mt-2">Extrato</h5>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="investimentos" class="text-decoration-none text-dark">
                <div class="menu-card text-center">
                    <i class="bi bi-graph-up-arrow"></i>
                    <h5 class="mt-2">Investimentos</h5>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="meus-investimentos" class="text-decoration-none text-dark">
                <div class="menu-card text-center">
                    <i class="bi bi-boxes"></i>
                    <h5 class="mt-2">Meus Investimentos</h5>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="emprestimo" class="text-decoration-none text-dark">
                <div class="menu-card text-center">
                    <i class="bi bi-cash-coin"></i>
                    <h5 class="mt-2">Simular Empréstimo</h5>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="meus-dados" class="text-decoration-none text-dark">
                <div class="menu-card text-center">
                    <i class="bi bi-person-fill"></i>
                    <h5 class="mt-2">Meus Dados</h5>
                </div>
            </a>
        </div>

    </div>
</div>

<!-- FOOTER -->
<footer class="text-center py-4 mt-5" style="opacity:.6;">
    BitPay Blue • Segurança e tecnologia bancária de última geração.
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
