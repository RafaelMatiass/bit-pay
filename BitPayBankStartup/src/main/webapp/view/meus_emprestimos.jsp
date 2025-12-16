<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Emprestimo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="br.com.bitpay.model.Enums.StatusEmprestimo" %>
<%
    String contextPath = request.getContextPath();
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    
    List<Emprestimo> emprestimos = (List<Emprestimo>) request.getAttribute("emprestimos");
    
    String mensagemErro = (String) request.getSession().getAttribute("mensagemErro");
    if (mensagemErro != null) {
        request.getSession().removeAttribute("mensagemErro");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Meus Empréstimos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</head>
<body>
    
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container">
        <a class="navbar-brand" href="<%= contextPath %>/home">Bit Pay</a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <a href="<%= contextPath %>/home" class="btn btn-outline-light btn-sm">Voltar à Home</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <main class="container mt-4">
        <h2>Meus Empréstimos Ativos</h2>
        <p class="text-muted">Gerencie seus empréstimos e parcelas.</p>

        <% if (mensagemErro != null) { %>
            <div class="alert alert-danger">
                <%= mensagemErro %>
            </div>
        <% } %>

        <% if (emprestimos == null || emprestimos.isEmpty()) { %>
            <div class="alert alert-info mt-3">
                Você não possui empréstimos ativos no momento.
            </div>
            <a href="<%= contextPath %>/emprestimo" class="btn btn-primary">Simular um novo empréstimo</a>
        <% } else { %>
            <div class="table-responsive mt-4">
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>Empréstimo ID</th>
                            <th>Valor Total</th>
                            <th>Parcelas</th>
                            <th>Data Contratação</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Emprestimo emp : emprestimos) { 
                            String statusClass = "";
                            if (emp.getStatus() == StatusEmprestimo.ATIVO) {
                                statusClass = "badge bg-success";
                            } else if (emp.getStatus() == StatusEmprestimo.ATRASADO) {
                                statusClass = "badge bg-danger";
                            }
                        %>
                        <tr>
                            <td>#<%= emp.getId() %></td>
                            <td><%= nf.format(emp.getValorTotal()) %></td>
                            <td><%= emp.getNumerosParcelas() %>x</td>
                            <td><%= emp.getDataContratacao().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></td>
                            <td><span class="<%= statusClass %>"><%= emp.getStatus().toString() %></span></td>
                            <td>
                                <a href="<%= contextPath %>/detalhes-emprestimo?id=<%= emp.getId() %>" class="btn btn-sm btn-info">
                                    Ver Parcelas
                                </a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>