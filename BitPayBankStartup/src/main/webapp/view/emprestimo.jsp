<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Conta"%>
<%@ page import="br.com.bitpay.model.ParcelaEmprestimo" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Date" %>

<%! private static final int SCALE = 2; %>

<%
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt","BR"));
    NumberFormat decimalFormat = NumberFormat.getNumberInstance(Locale.of("pt","BR"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    decimalFormat.setMinimumFractionDigits(4);
    decimalFormat.setMaximumFractionDigits(6);

    Conta conta = (Conta) session.getAttribute("conta");
    if (conta == null) {
        response.sendRedirect("login");
        return;
    }

    List<ParcelaEmprestimo> simulacao = (List<ParcelaEmprestimo>) request.getAttribute("simulacao");

    BigDecimal valorTotal = (BigDecimal) request.getAttribute("valorTotal");
    BigDecimal valorEntrada = (BigDecimal) request.getAttribute("valorEntrada");
    BigDecimal saldoDevedorEfetivo = (BigDecimal) request.getAttribute("saldoDevedorEfetivo");
    BigDecimal taxaJuros = (BigDecimal) request.getAttribute("taxaJuros");
    Integer numeroParcelas = (Integer) request.getAttribute("numeroParcelas");
    String mensagemErro = (String) request.getAttribute("mensagemErro");

    BigDecimal totalAmortizacao = BigDecimal.ZERO;
    BigDecimal totalJuros = BigDecimal.ZERO;
    BigDecimal totalParcela = BigDecimal.ZERO;

    if (simulacao != null) {
        for (ParcelaEmprestimo p : simulacao) {
            totalAmortizacao = totalAmortizacao.add(p.getValorAmortizacao());
            totalJuros = totalJuros.add(p.getValorJuros());
            totalParcela = totalParcela.add(p.getValorParcela());
        }
        totalJuros = totalParcela.subtract(saldoDevedorEfetivo);
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<title>BitPay • Simular Empréstimo</title>
<base href="<%= request.getContextPath() %>/">

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
    padding: 28px;
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
        <h2 class="page-title">Simulador de Empréstimo (SAC)</h2>
        <a href="home" class="btn btn-outline-primary">Voltar</a>
    </div>

    <form action="emprestimo" method="POST" class="card-modern mb-4">
        <div class="row g-4">
            <div class="col-md-3">
                <label class="form-label fw-semibold">Entrada</label>
                <input type="number" step="0.01" min="0" name="entrada" class="form-control form-control-lg" value="0.00" required>
            </div>
            <div class="col-md-3">
                <label class="form-label fw-semibold">Valor Total</label>
                <input type="number" step="0.01" min="1000" name="valor" class="form-control form-control-lg" required>
            </div>
            <div class="col-md-3">
                <label class="form-label fw-semibold">Parcelas</label>
                <input type="number" min="2" max="60" name="parcelas" class="form-control form-control-lg" required>
            </div>
            <div class="col-md-3">
                <label class="form-label fw-semibold">Taxa (% a.m.)</label>
                <input type="number" step="0.0001" min="0.0001" name="taxaJuros" class="form-control form-control-lg" required>
            </div>
        </div>

        <button class="btn btn-primary w-100 mt-4">Simular Empréstimo</button>
    </form>

    <% if (simulacao != null && !simulacao.isEmpty()) { %>

    <div class="card-modern mb-4">
        <h4 class="fw-bold mb-3">Resumo da Simulação</h4>
        <div class="row text-center">
            <div class="col-md-4">
                <small class="text-muted">Saldo Devedor</small>
                <h4 class="fw-bold text-primary"><%= currencyFormat.format(saldoDevedorEfetivo) %></h4>
            </div>
            <div class="col-md-4">
                <small class="text-muted">Total com Juros</small>
                <h4 class="fw-bold"><%= currencyFormat.format(totalParcela) %></h4>
            </div>
            <div class="col-md-4">
                <small class="text-muted">Total de Juros</small>
                <h4 class="fw-bold text-danger"><%= currencyFormat.format(totalJuros) %></h4>
            </div>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-modern align-middle">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Vencimento</th>
                    <th>Amortização</th>
                    <th>Juros</th>
                    <th>Parcela</th>
                    <th>Saldo</th>
                </tr>
            </thead>
            <tbody>
                <% for (ParcelaEmprestimo p : simulacao) {
                    Date d = Date.from(p.getDataVencimento().atStartOfDay(ZoneId.systemDefault()).toInstant());
                %>
                <tr>
                    <td><%= p.getNumeroParcela() %></td>
                    <td><%= dateFormat.format(d) %></td>
                    <td><%= currencyFormat.format(p.getValorAmortizacao()) %></td>
                    <td><%= currencyFormat.format(p.getValorJuros()) %></td>
                    <td class="fw-bold"><%= currencyFormat.format(p.getValorParcela()) %></td>
                    <td><%= currencyFormat.format(p.getSaldoDevedorAtual()) %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <form action="contratar-emprestimo" method="POST" class="mt-4">
        <input type="hidden" name="valorTotal" value="<%= valorTotal %>">
        <input type="hidden" name="valorEntrada" value="<%= valorEntrada %>">
        <input type="hidden" name="taxaJuros" value="<%= taxaJuros %>">
        <input type="hidden" name="parcelas" value="<%= numeroParcelas %>">
        <button class="btn btn-success btn-lg w-100">
            Contratar Empréstimo (<%= currencyFormat.format(totalParcela) %>)
        </button>
    </form>

    <% } %>

</div>

<script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>

<% if (mensagemErro != null) { %>
<script>
Toastify({
    text: "<%= mensagemErro %>",
    backgroundColor: "linear-gradient(135deg,#dc3545,#ff6b6b)",
    duration: 4000
}).showToast();
</script>
<% } %>

</body>
</html>
