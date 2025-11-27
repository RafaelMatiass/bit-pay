<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Cliente" %>
<%@ page import="br.com.bitpay.model.Conta" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat, java.util.Locale" %>

<%

    Cliente cliente = (Cliente) session.getAttribute("cliente");
    Conta conta = (Conta) session.getAttribute("conta");

    if (cliente == null || conta == null) {
        response.sendRedirect("login"); 
        return;
    }
    String nomeUsuario = cliente.getNome();
    BigDecimal saldoConta = conta.getSaldo(); 
    
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
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
            <li class="nav-item ms-3">
              <a href="logout" class="btn btn-outline-light btn-sm">Sair</a>
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
        <h3 classV="mb-3">O que você deseja fazer?</h3>
        
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
                <a href="extrato-page" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-receipt-cutoff fs-1"></i>
                        <h5>Gerar Extrato</h5>
                    </div>
                </a>
            </div>

            <div class="col-md-4">
                <a href="investimentos-page" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-graph-up-arrow fs-1"></i>
                        <h5>Investimentos</h5>
                    </div>
                </a>
            </div>

            <div class="col-md-4">
                <a href="emprestimo-page" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-cash-coin fs-1"></i>
                        <h5>Simular Empréstimo</h5>
                    </div>
                </a>
            </div>

            <div class="col-md-4">
                <a href="meus-dados-page" class="text-decoration-none">
                    <div class="card p-3 text-center h-100">
                        <i class="bi bi-person-fill fs-1"></i>
                        <h5>Meus Dados Pessoais</h5>
                    </div>
                </a>
            </div>
            
        </div>
    </main>

</body>
</html>