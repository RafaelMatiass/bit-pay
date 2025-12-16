<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Emprestimo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="br.com.bitpay.model.Enums.StatusEmprestimo" %>

<%
    String contextPath = request.getContextPath();
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt","BR"));

    List<Emprestimo> emprestimos = (List<Emprestimo>) request.getAttribute("emprestimos");
    String mensagemErro = (String) session.getAttribute("mensagemErro");
    if (mensagemErro != null) session.removeAttribute("mensagemErro");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<title>BitPay • Meus Empréstimos</title>

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
</style>
</head>

<body>

<div class="container py-5" style="max-width:1100px;">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="page-title">Meus Empréstimos</h2>
        <a href="<%= contextPath %>/home" class="btn btn-outline-primary">Home</a>
    </div>

    <% if (emprestimos == null || emprestimos.isEmpty()) { %>
        <div class="card-modern text-center">
            <p class="text-muted mb-3">Você não possui empréstimos ativos.</p>
            <a href="<%= contextPath %>/emprestimo" class="btn btn-primary">
                Simular Empréstimo
            </a>
        </div>
    <% } else { %>

    <div class="row g-4">
        <% for (Emprestimo e : emprestimos) { %>
        <div class="col-md-6">
            <div class="card-modern">
                <h5 class="fw-bold mb-2">Empréstimo #<%= e.getId() %></h5>
                <p class="text-muted mb-1">
                    Valor: <strong><%= nf.format(e.getValorTotal()) %></strong>
                </p>
                <p class="text-muted mb-1">
                    Parcelas: <%= e.getNumerosParcelas() %>x
                </p>
                <p>
                    Status:
                    <span class="badge bg-<%= e.getStatus()==StatusEmprestimo.ATIVO?"success":"danger" %>">
                        <%= e.getStatus().toString() %>
                    </span>
                </p>
                <a href="<%= contextPath %>/detalhes-emprestimo?id=<%= e.getId() %>"
                   class="btn btn-outline-primary w-100 mt-2">
                    Ver Detalhes
                </a>
            </div>
        </div>
        <% } %>
    </div>

    <% } %>

</div>

<script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>

<% if (mensagemErro != null) { %>
<script>
Toastify({ text:"<%= mensagemErro %>", duration:3500,
backgroundColor:"linear-gradient(135deg,#dc3545,#ff6b6b)" }).showToast();
</script>
<% } %>

</body>
</html>
