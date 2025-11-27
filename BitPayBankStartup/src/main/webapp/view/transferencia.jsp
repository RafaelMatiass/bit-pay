<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Conta" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat, java.util.Locale" %>

<%
    Conta conta = (Conta) session.getAttribute("conta");
    
    if (conta == null) {
        response.sendRedirect("login"); 
        return;
    }
    
    BigDecimal saldo = conta.getSaldo();
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    String saldoAtual = nf.format(saldo);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Transferência</title>
    <base href="<%= request.getContextPath() %>/">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white text-center">
                        <h4>Realizar Transferência</h4>
                    </div>
                    <div class="card-body">
                        <p class="mb-4">Seu Saldo: <strong><%= saldoAtual %></strong> (Conta: <%= conta.getNumeroConta() %>)</p>
                        
                        <form action="transferencia" method="POST">
                            
                            <div class="mb-3">
                                <label for="contaDestino" class="form-label">Conta Destino</label>
                                <input type="text" class="form-control" id="contaDestino" name="contaDestino" placeholder="Número da Conta (Ex. 10011-1)" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="valorTransferencia" class="form-label">Valor a Transferir</label>
                                <input type="number" step="0.01" min="0.01" class="form-control" id="valorTransferencia" name="valorTransferencia" placeholder="R$ 0,00" required>
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-success">
							    <i class="bi bi-send-fill"></i> Transferir
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