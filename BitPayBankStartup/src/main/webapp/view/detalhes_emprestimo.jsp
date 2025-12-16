<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Emprestimo" %>
<%@ page import="br.com.bitpay.model.ParcelaEmprestimo" %>
<%@ page import="br.com.bitpay.model.Enums.StatusParcelasEmprestimo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.LocalDate" %>

<%
    String contextPath = request.getContextPath();
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt","BR"));

    Emprestimo emprestimo = (Emprestimo) request.getAttribute("emprestimo");
    List<ParcelaEmprestimo> parcelas = (emprestimo != null) ? emprestimo.getParcelas() : null;

    String mensagemSucesso = (String) session.getAttribute("mensagemSucesso");
    String mensagemErro = (String) session.getAttribute("mensagemErro");
    if (mensagemSucesso != null) session.removeAttribute("mensagemSucesso");
    if (mensagemErro != null) session.removeAttribute("mensagemErro");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<title>BitPay • Detalhes do Empréstimo</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">

<style>
:root {
    --primary:#0d6efd;
    --bg:#f5f7fc;
    --radius:22px;
}

body {
    background: radial-gradient(circle at top left,#4c8dff33,#0d6efd11 40%,#0a1a33 100%), var(--bg);
    min-height: 100vh;
    font-family: Inter, sans-serif;
}

.card-modern {
    background: linear-gradient(135deg,#ffffff,#eff4ff);
    border-radius: var(--radius);
    padding: 26px;
    border: 1px solid #0d6efd25;
    box-shadow: 0 18px 50px #0d6efd33;
    animation: fadeUp .6s ease;
}

@keyframes fadeUp {
    from { opacity:0; transform: translateY(20px); }
    to { opacity:1; transform: translateY(0); }
}

.page-title {
    font-weight: 800;
    color:#0a2a57;
}

.table-modern {
    border-radius:18px;
    overflow:hidden;
    box-shadow: 0 18px 50px #0d6efd33;
}

.table-modern thead {
    background:#0d6efd;
    color:white;
}
</style>
</head>

<body>

<div class="container py-5" style="max-width:1200px;">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="page-title">Empréstimo #<%= emprestimo != null ? emprestimo.getId() : "" %></h2>
        <a href="<%= contextPath %>/meus-emprestimos" class="btn btn-outline-primary">Voltar</a>
    </div>

    <% if (emprestimo == null) { %>
        <div class="card-modern text-center">
            <h5 class="text-danger">Empréstimo não encontrado.</h5>
        </div>
    <% } else { %>

    <div class="card-modern mb-4">
        <div class="row text-center">
            <div class="col-md-4">
                <small class="text-muted">Valor Principal</small>
                <h4 class="fw-bold"><%= nf.format(emprestimo.getValorTotal()) %></h4>
            </div>
            <div class="col-md-4">
                <small class="text-muted">Parcelas</small>
                <h4 class="fw-bold"><%= emprestimo.getNumerosParcelas() %>x</h4>
            </div>
            <div class="col-md-4">
                <small class="text-muted">Taxa</small>
                <h4 class="fw-bold"><%= emprestimo.getTaxaJuros() %>% a.m.</h4>
            </div>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-modern align-middle">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Vencimento</th>
                    <th>Parcela</th>
                    <th>Amortização</th>
                    <th>Juros</th>
                    <th>Status</th>
                    <th>Ação</th>
                </tr>
            </thead>
            <tbody>
                <% for (ParcelaEmprestimo p : parcelas) {
                    boolean aberta = p.getStatus() == StatusParcelasEmprestimo.ABERTA;
                    boolean vencida = aberta && p.getDataVencimento().isBefore(LocalDate.now());
                %>
                <tr>
                    <td><%= p.getNumeroParcela() %></td>
                    <td><%= p.getDataVencimento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></td>
                    <td class="fw-bold"><%= nf.format(p.getValorParcela()) %></td>
                    <td><%= nf.format(p.getValorAmortizacao()) %></td>
                    <td><%= nf.format(p.getValorJuros()) %></td>
                    <td>
                        <% if (aberta) { %>
                            <span class="badge bg-<%= vencida ? "danger" : "warning" %>">
                                <%= vencida ? "VENCIDA" : "ABERTA" %>
                            </span>
                        <% } else { %>
                            <span class="badge bg-success">PAGA</span>
                        <% } %>
                    </td>
                    <td>
                        <% if (aberta) { %>
                        <form action="<%= contextPath %>/pagar-parcela" method="POST">
                            <input type="hidden" name="idEmprestimo" value="<%= emprestimo.getId() %>">
                            <input type="hidden" name="idParcela" value="<%= p.getId() %>">
                            <button class="btn btn-success btn-sm w-100">Pagar</button>
                        </form>
                        <% } else { %>
                            <small class="text-muted">Liquidada</small>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <% } %>

</div>

<script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>

<% if (mensagemSucesso != null) { %>
<script>
Toastify({ text:"<%= mensagemSucesso %>", duration:3500,
backgroundColor:"linear-gradient(135deg,#0d6efd,#4c8dff)" }).showToast();
</script>
<% } %>

<% if (mensagemErro != null) { %>
<script>
Toastify({ text:"<%= mensagemErro %>", duration:3500,
backgroundColor:"linear-gradient(135deg,#dc3545,#ff6b6b)" }).showToast();
</script>
<% } %>

</body>
</html>
