<!-- Updated JSP home page with login modal added -->
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

    String nomeUsuario = cliente.getNome();
    BigDecimal saldoConta = conta.getSaldo(); 

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    String saldoFormatado = nf.format(saldoConta);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Home</title>
    <base href="<%= request.getContextPath() %>/">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</head>
<body>
    
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container">
        <a class="navbar-brand" href="home">Bit Pay</a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <span class="navbar-text">
                Olá, <%= nomeUsuario.split(" ")[0] %>
              </span>
            </li>
<!--             <li class="nav-item ms-3">
              <button class="btn btn-outline-light btn-sm" data-bs-toggle="modal" data-bs-target="#modalLogin">Login</button>
            </li> -->
            <li class="nav-item ms-3">
              <a href="logout" class="btn btn-outline-danger btn-sm">Sair</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <header class="container mt-4 p-5 bg-light rounded">
        <p class="h5">Saldo em Conta Corrente (<%= conta.getNumeroConta() %>)</p>
        <p class="display-4"><%= saldoFormatado %></p>
    </header>

    <main class="container mt-4">
        <h3 class="mb-3">O que você deseja fazer?</h3>
        
        <div class="row g-3">
            <div class="col-md-4">
                <a href="transferencia" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-send-fill fs-1"></i>
                        <h5>Fazer Transferência</h5>
                    </div>
                </a>
            </div>

            <div class="col-md-4">
                <a href="deposito" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-cash-stack fs-1"></i>
                        <h5>Fazer Depósito</h5>
                    </div>
                </a>
            </div>
            
            <div class="col-md-4">
                <a href="extrato" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-receipt-cutoff fs-1"></i>
                        <h5>Extrato</h5>
                    </div>
                </a>
            </div>

            <div class="col-md-4">
                <a href="investimentos" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-graph-up-arrow fs-1"></i>
                        <h5>Investimentos</h5>
                    </div>
                </a>
            </div>
            
              <div class="col-md-4">
                <a href="meus-investimentos" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-boxes fs-1"></i>
                        <h5>Meus Investimentos</h5>
                    </div>
                </a>
            </div>

            <div class="col-md-4">
                <a href="emprestimo" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-cash-coin fs-1"></i>
                        <h5>Simular Empréstimo</h5>
                    </div>
                </a>
            </div>

            <div class="col-md-4">
                <a href="meus-dados" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-person-fill fs-1"></i>
                        <h5>Meus Dados Pessoais</h5>
                    </div>
                </a>
            </div>
            
        </div>
    </main>


<!-- MODAL LOGIN -->
<div class="modal fade" id="modalLogin" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content p-3">

      <div class="modal-header border-0">
        <h5 class="modal-title text-primary">Acessar Conta</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>

      <div class="modal-body">

        <form action="login" method="POST">

            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" id="email" name="email" class="form-control" required>
            </div>

            <div class="mb-3">
                <label for="senha" class="form-label">Senha</label>
                <input type="password" id="senha" name="senha" class="form-control" required>
            </div>

            <div class="d-grid mt-3">
                <button class="btn btn-primary" type="submit">Entrar</button>
            </div>

        </form>

      </div>

    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>