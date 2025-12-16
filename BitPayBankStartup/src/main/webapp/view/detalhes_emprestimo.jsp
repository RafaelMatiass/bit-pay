<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Emprestimo" %>
<%@ page import="br.com.bitpay.model.ParcelaEmprestimo" %>
<%@ page import="br.com.bitpay.model.Enums.StatusParcelasEmprestimo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.time.LocalDate" %>
<%
    String contextPath = request.getContextPath();
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    
    // Coleta dos atributos enviados pelo Controller
    Emprestimo emprestimo = (Emprestimo) request.getAttribute("emprestimo");
    List<ParcelaEmprestimo> parcelas = (emprestimo != null) ? emprestimo.getParcelas() : null;
    
    // Coleta mensagens da sessão (POST-REDIRECT-GET)
    String mensagemSucesso = (String) request.getSession().getAttribute("mensagemSucesso");
    String mensagemErro = (String) request.getSession().getAttribute("mensagemErro");
    if (mensagemSucesso != null) request.getSession().removeAttribute("mensagemSucesso");
    if (mensagemErro != null) request.getSession().removeAttribute("mensagemErro");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Detalhes do Empréstimo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        .detalhes-header {
            background-color: #f8f9fa;
            border-bottom: 1px solid #e9ecef;
            padding-bottom: 15px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    
  
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container">
        <a class="navbar-brand" href="<%= contextPath %>/home">Bit Pay</a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <a href="<%= contextPath %>/meus-emprestimos" class="btn btn-outline-light btn-sm">Voltar aos Empréstimos</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <main class="container mt-4">
        
        <% if (emprestimo == null) { %>
            <div class="alert alert-danger">Empréstimo não encontrado ou acesso negado.</div>
            <a href="<%= contextPath %>/meus-emprestimos" class="btn btn-primary">Voltar à Listagem</a>
            
        <% } else {%>

        <div class="detalhes-header">
            <h2>Detalhes do Empréstimo #<%= emprestimo.getId() %></h2>
            <p>Contratado em: <strong><%= emprestimo.getDataContratacao().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></strong></p>
            <p>Valor Principal: <strong><%= nf.format(emprestimo.getValorTotal().subtract(emprestimo.getValorEntrada())) %></strong> | Taxa: <strong><%= emprestimo.getTaxaJuros().toPlainString() %>% a.m.</strong></p>
        </div>

        <% if (mensagemSucesso != null) { %>
            <div class="alert alert-success"><%= mensagemSucesso %></div>
        <% } %>
        <% if (mensagemErro != null) { %>
            <div class="alert alert-danger"><%= mensagemErro %></div>
        <% } %>

        <h3>Plano de Pagamento (<%= emprestimo.getNumerosParcelas() %> Parcelas)</h3>

        <% if (parcelas == null || parcelas.isEmpty()) { %>
            <div class="alert alert-warning">Nenhuma parcela encontrada para este empréstimo.</div>
        <% } else { %>
            <div class="table-responsive">
                <table class="table table-bordered table-striped table-sm align-middle">
                    <thead class="table-primary">
                        <tr>
                            <th>Nº</th>
                            <th>Vencimento</th>
                            <th>Valor Parcela</th>
                            <th>Amortização</th>
                            <th>Juros</th>
                            <th>Status</th>
                            <th>Ação</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (ParcelaEmprestimo parcela : parcelas) { 
                            boolean estaAberta = parcela.getStatus() == StatusParcelasEmprestimo.ABERTA;
                            boolean estaVencida = estaAberta && parcela.getDataVencimento().isBefore(LocalDate.now());
                            
                            String statusText = parcela.getStatus().getDescricao();
                            String statusClass = "text-success"; 
                            if (estaAberta) {
                                statusClass = estaVencida ? "text-danger fw-bold" : "text-warning";
                                statusText = estaVencida ? "VENCIDA" : "ABERTA";
                            }
                        %>
                        <tr>
                            <td><%= parcela.getNumeroParcela() %></td>
                            <td><%= parcela.getDataVencimento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></td>
                            <td class="fw-bold"><%= nf.format(parcela.getValorParcela()) %></td>
                            <td><%= nf.format(parcela.getValorAmortizacao()) %></td>
                            <td><%= nf.format(parcela.getValorJuros()) %></td>
                            <td><span class="<%= statusClass %>"><%= statusText %></span></td>
                            <td>
                                <% if (estaAberta) { %>
                                   
                                    <form action="<%= contextPath %>/pagar-parcela" method="POST" style="display: inline;">
                                        <input type="hidden" name="idEmprestimo" value="<%= emprestimo.getId() %>">
                                        <input type="hidden" name="idParcela" value="<%= parcela.getId() %>">
                                        
                                     
                                        <button type="submit" class="btn btn-sm btn-success" 
                                                title="Pagar Parcela #<%= parcela.getNumeroParcela() %>">
                                            Pagar
                                        </button>
                                    </form>
                                <% } else { %>
                                    <span class="text-muted small">Liquidada em <%= parcela.getDataPagamento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></span>
                                <% } %>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
        <% } %>
        
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>