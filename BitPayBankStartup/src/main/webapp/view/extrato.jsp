<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="br.com.bitpay.model.Movimentacao" %>
<%@ page import="br.com.bitpay.model.Conta" %>
<%@ page import="br.com.bitpay.model.Enums.TipoMovimento" %> 
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%
    Conta conta = (Conta) session.getAttribute("conta");
    if (conta == null) {
        response.sendRedirect("index.jsp"); 
        return;
    }
    List<Movimentacao> extrato = (List<Movimentacao>) request.getAttribute("extrato");
    String dataInicial = (String) request.getAttribute("dataInicial");
    String dataFinal = (String) request.getAttribute("dataFinal");
    String erro = (String) request.getAttribute("erro");
    
    Boolean isFilterSubmitted = (Boolean) request.getAttribute("filterSubmitted");
    if (isFilterSubmitted == null) {
        isFilterSubmitted = false; 
    }

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Extrato da Conta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet" 
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" 
          crossorigin="anonymous">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Extrato da Conta</h1>
        <p class="lead">Conta: <strong><%= conta.getNumeroConta() %></strong></p>

        <% if (erro != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= erro %>
            </div>
        <% } %>

        <div class="card p-4 mb-4">
            <h2 class="h5 card-title">Filtro por Período</h2>
            <form action="extrato" method="post" class="row g-3 align-items-end">
                
                <div class="col-md-4">
                    <label for="dataInicial" class="form-label">Data Inicial:</label>
                    <input type="date" id="dataInicial" name="dataInicial" class="form-control" required value="<%= dataInicial != null ? dataInicial : "" %>">
                </div>
                
                <div class="col-md-4">
                    <label for="dataFinal" class="form-label">Data Final:</label>
                    <input type="date" id="dataFinal" name="dataFinal" class="form-control" required value="<%= dataFinal != null ? dataFinal : "" %>">
                </div>
                
                <div class="col-md-4 d-flex gap-2">
                    <button type="submit" name="acao" value="buscar" class="btn btn-primary">Buscar Extrato</button>
                    
                    <% if (extrato != null && !extrato.isEmpty()) { %>
                        <button type="submit" name="acao" value="gerarPDF" class="btn btn-success">Gerar PDF</button>
                    <% } %>
                </div>
            </form>
        </div> <% if (extrato != null) { %>
            <% if (extrato.isEmpty()) { %>
                <div class="alert alert-info" role="alert">
                    Nenhuma movimentação encontrada para o período selecionado.
                </div>
            <% } else { %>
                <% 
                    String title;
                    if (isFilterSubmitted) {
                        title = "Resultado do Extrato (" + dataInicial + " a " + dataFinal + ")";
                    } else {
                        title = "Histórico dos últimos 30 dias";
                    }
                %>
                
                <h3 class="mt-4"><%= title %></h3>
                
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-sm">
                        <thead class="table-dark">
                            <tr>
                                <th>Data</th>                             
                                <th>Nome</th>
                                <th>Nº Conta</th>
                                <th>Tipo</th>
                                <th class="text-end">Valor</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Movimentacao mov : extrato) { %>
                                <tr>
                                    <td><%= mov.getDataMovimento() %></td>                                   
                                    
                                    <td>
                                        <% 
                                            String nome = mov.getNomeClienteDestino();
                                            
                                            if (mov.getTipoMovimento() == TipoMovimento.TRANSFERENCIA_ENVIADA) {
                                                out.print(nome != null ? nome : "Destinatário Inválido");
                                            } else if (mov.getTipoMovimento() == TipoMovimento.TRANSFERENCIA_RECEBIDA) {
                                                out.print(nome != null ? nome : "Remetente Inválido");
                                            } else if (mov.getTipoMovimento() == TipoMovimento.DEPOSITO) {
                                                out.print("Própria Conta");
                                            } else if (mov.getTipoMovimento() == TipoMovimento.SAQUE) {
                                                out.print("Caixa Eletrônico");
                                            } else {
                                                out.print("-");
                                            }
                                        %>
                                    </td>
                                    
                                                                      
                                    <td>
                                        <% 
                                            String numeroConta = mov.getNumeroContaDestino();
                                            
                                            if (mov.getTipoMovimento() == TipoMovimento.TRANSFERENCIA_ENVIADA) {
                                                out.print(numeroConta != null ? numeroConta : "Externo");
                                            } else if (mov.getTipoMovimento() == TipoMovimento.TRANSFERENCIA_RECEBIDA) {
                                                out.print(numeroConta != null ? numeroConta : "Externo");
                                            } else if (mov.getTipoMovimento() == TipoMovimento.DEPOSITO || mov.getTipoMovimento() == TipoMovimento.SAQUE) {
                                                out.print("-"); 
                                            } else {
                                                out.print("-");
                                            }
                                        %>
                                    </td>
                                    
                                    <td><%= mov.getTipoMovimento().getDescricao() %></td>
                                    
                                    <td class="text-end <%= mov.getTipoMovimento().name().contains("ENVIADA") || mov.getTipoMovimento().name().equals("SAQUE") ? "text-danger" : "text-success" %>">
                                        <%= nf.format(mov.getValor()) %>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        <% } %>
        
        <div class="mt-4">
            <a href="home" class="btn btn-secondary">Voltar para a Home</a>
        </div>
    </div> <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" 
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" 
            crossorigin="anonymous"></script>
</body>
</html>