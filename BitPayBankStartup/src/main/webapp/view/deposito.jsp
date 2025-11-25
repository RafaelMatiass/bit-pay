<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Conta"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.text.NumberFormat, java.util.Locale"%>

<%
    Conta conta = (Conta) session.getAttribute("conta");
	BigDecimal saldo = conta.getSaldo();
	NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	String saldoAtual = nf.format(saldo);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Depósito</title>
    <base href="<%= request.getContextPath() %>/">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white text-center">
                        <h4>Realizar Depósito</h4>
                    </div>
                    <div class="card-body">
                    <p>Saldo Atual: <strong><%= saldoAtual %></strong></p>
                        <form action="deposito" method="POST">
                            <div class="mb-3">
                                <label for="valor" class="form-label">Valor do Depósito</label>
                                <input type="number" step="0.01" min="0.01" class="form-control" id="valor" name="valor" placeholder="0,00" required>
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-success">
							    <i class="bi bi-cash-stack"></i> Depositar
							</button>
                            </div>
                        </form>
                        <hr>
                        <a href="home" class="btn btn-secondary w-100">Voltar</a>

                        <!-- Mensagem de feedback -->
                        <% 
                            String mensagem = (String) request.getAttribute("mensagem");
                            String tipo = (String) request.getAttribute("tipoMensagem");
                            if (mensagem != null) { 
                        %>
                            <div class="alert alert-<%= tipo %> mt-3" role="alert">
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
