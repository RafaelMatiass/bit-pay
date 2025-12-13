<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Cliente" %>
<%@ page import="br.com.bitpay.model.Gerente" %>
<%@ page import="java.util.List" %>

<%
    Object usuarioLogado = session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !(usuarioLogado instanceof Gerente)) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    Gerente gerenteLogado = (Gerente) usuarioLogado;

    List<Cliente> clientesPendentes = (List<Cliente>) request.getAttribute("clientesPendentes");
    List<Cliente> clientesAprovados = (List<Cliente>) request.getAttribute("clientesAprovados");
    List<Cliente> clientesRecusados = (List<Cliente>) request.getAttribute("clientesRecusados");

    if (clientesPendentes == null) clientesPendentes = java.util.Collections.emptyList();
    if (clientesAprovados == null) clientesAprovados = java.util.Collections.emptyList();
    if (clientesRecusados == null) clientesRecusados = java.util.Collections.emptyList();
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<title>BitPay Blue - Painel do Gerente</title>
<base href="<%= request.getContextPath() %>/">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<style>
    body {
        background: linear-gradient(135deg, #e8f0ff, #f4f8ff, #e9f1ff);
        min-height: 100vh;
        font-family: Inter, sans-serif;
    }

    .navbar {
        backdrop-filter: blur(10px);
        background: #0a2a57 !important;
    }

    .main-title {
        color: #0a2a57;
        font-weight: 800;
    }

    .card-counter {
        border-radius: 18px;
        padding: 22px;
        color: white;
    }
    .bg-blue { background: #0d6efd; }
    .bg-green { background: #0abf5b; }
    .bg-red { background: #d9534f; }

    table thead {
        background: #0d6efd;
        color: white;
    }

    table tbody tr:hover {
        background: #f0f6ff;
        transition: 0.2s;
    }

    .btn-approve {
        background: #0abf5b;
        border: none;
        color: white;
    }

    .btn-reject {
        background: #d9534f;
        border: none;
        color: white;
    }

    .section-title {
        color: #0a2a57;
        font-weight: 700;
        margin-top: 40px;
    }
</style>

</head>
<body>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-dark shadow">
  <div class="container">
    <a class="navbar-brand fw-bold fs-4">BitPay Blue • Admin</a>

    <ul class="navbar-nav ms-auto align-items-center">
        <li class="nav-item me-3 text-white">
            Olá, <%= gerenteLogado.getNome().split(" ")[0] %>
        </li>
        <li class="nav-item">
            <a href="logout" class="btn btn-outline-light btn-sm">Sair</a>
        </li>
    </ul>
  </div>
</nav>

<div class="container py-5">

    <!-- TÍTULO -->
    <h1 class="main-title mb-4"><i class="bi bi-shield-lock-fill"></i> Painel do Gerente</h1>
    <p class="text-muted mb-5">Gerencie contas pendentes, aprovadas e recusadas.</p>

    <!-- CARDS RESUMO -->
    <div class="row g-4 mb-5">

        <div class="col-md-4">
            <div class="card-counter bg-blue shadow-sm">
                <h3><%= clientesPendentes.size() %></h3>
                <p>Contas Pendentes</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card-counter bg-green shadow-sm">
                <h3><%= clientesAprovados.size() %></h3>
                <p>Contas Aprovadas</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card-counter bg-red shadow-sm">
                <h3><%= clientesRecusados.size() %></h3>
                <p>Contas Recusadas</p>
            </div>
        </div>
    </div>

    <!-- TABELA PENDENTES -->
    <h3 class="section-title">Contas Pendentes</h3>
    <div class="table-responsive shadow-sm bg-white rounded p-3">
        <table class="table table-hover align-middle">
            <thead>
                <tr>
                    <th>Nº Conta</th>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Email</th>
                    <th class="text-center">Ações</th>
                </tr>
            </thead>
            <tbody>
                <% if (clientesPendentes.isEmpty()) { %>
                    <tr><td colspan="5" class="text-center text-muted py-3">Nenhuma conta pendente.</td></tr>
                <% } else { %>
                    <% for (Cliente cliente : clientesPendentes) { %>
                    <tr>
                        <td><%= cliente.getConta().getNumeroConta() %></td>
                        <td><%= cliente.getNome() %></td>
                        <td><%= cliente.getCpf() %></td>
                        <td><%= cliente.getEmail() %></td>
                        <td class="text-center">

                            <!-- Aprovar -->
                            <form action="aprovar-conta" method="POST" class="d-inline">
                                <input type="hidden" name="idConta" value="<%= cliente.getConta().getContaId() %>">
                                <button class="btn btn-approve btn-sm">
                                    <i class="bi bi-check-lg"></i> Aprovar
                                </button>
                            </form>

                            <!-- Recusar -->
                            <form action="recusar-conta" method="POST" class="d-inline">
                                <input type="hidden" name="idConta" value="<%= cliente.getConta().getContaId() %>">
                                <button class="btn btn-reject btn-sm ms-2">
                                    <i class="bi bi-x-lg"></i> Recusar
                                </button>
                            </form>

                        </td>
                    </tr>
                    <% } %>
                <% } %>
            </tbody>
        </table>
    </div>

    <!-- TABELA APROVADOS -->
    <h3 class="section-title">Clientes Aprovados</h3>
    <div class="table-responsive shadow-sm bg-white rounded p-3">
        <table class="table table-hover align-middle">
            <thead>
                <tr>
                    <th>Nº Conta</th>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                <% if (clientesAprovados.isEmpty()) { %>
                    <tr><td colspan="4" class="text-center text-muted py-3">Nenhum aprovado ainda.</td></tr>
                <% } else { 
                       for (Cliente cliente : clientesAprovados) { %>
                       <tr>
                           <td><%= cliente.getConta().getNumeroConta() %></td>
                           <td><%= cliente.getNome() %></td>
                           <td><%= cliente.getCpf() %></td>
                           <td><%= cliente.getEmail() %></td>
                       </tr>
                <% } } %>
            </tbody>
        </table>
    </div>

    <!-- TABELA RECUSADOS -->
    <h3 class="section-title">Clientes Recusados</h3>
    <div class="table-responsive shadow-sm bg-white rounded p-3 mb-5">
        <table class="table table-hover align-middle">
            <thead>
                <tr>
                    <th>Nº Conta</th>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                <% if (clientesRecusados.isEmpty()) { %>
                    <tr><td colspan="4" class="text-center text-muted py-3">Nenhuma conta recusada.</td></tr>
                <% } else { 
                       for (Cliente cliente : clientesRecusados) { %>
                       <tr>
                           <td><%= cliente.getConta().getNumeroConta() %></td>
                           <td><%= cliente.getNome() %></td>
                           <td><%= cliente.getCpf() %></td>
                           <td><%= cliente.getEmail() %></td>
                       </tr>
                <% } } %>
            </tbody>
        </table>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
