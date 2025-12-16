<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="br.com.bitpay.model.TipoInvestimento" %>
<%@ page import="br.com.bitpay.model.AplicacaoInvestimento" %>

<%
    NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.of("pt","BR"));

    BigDecimal saldoConta = (BigDecimal) request.getAttribute("saldoConta");
    if (saldoConta == null) saldoConta = BigDecimal.ZERO;

    List<TipoInvestimento> tipos =
        (List<TipoInvestimento>) request.getAttribute("tiposInvestimento");

    List<AplicacaoInvestimento> portfolio =
        (List<AplicacaoInvestimento>) request.getAttribute("portfolio");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<title>BitPay • Caixinhas</title>
<base href="<%= request.getContextPath() %>/">

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

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
    padding: 24px;
}

.card-modern {
    background: linear-gradient(135deg, #ffffff, #eff4ff);
    border-radius: var(--radius);
    padding: 26px;
    border: 1px solid #0d6efd25;
    box-shadow: 0 18px 50px #0d6efd33;
    margin-bottom: 28px;
}

.saldo {
    font-size: 26px;
    font-weight: 800;
    color: #0a2a57;
}

.invest-option {
    border: 2px solid #e3eaff;
    border-radius: 18px;
    padding: 18px;
    cursor: pointer;
    transition: .2s;
}

.invest-option:hover {
    border-color: var(--primary);
    box-shadow: 0 10px 24px #0d6efd30;
}

.invest-option.active {
    border-color: var(--primary);
    background: #0d6efd08;
}

.badge-bit {
    background: var(--primary);
    color: white;
    border-radius: 14px;
    padding: 6px 12px;
    font-size: 12px;
    font-weight: 600;
}

.btn-primary {
    background: var(--primary);
    border: none;
    border-radius: 14px;
    padding: 12px;
    font-weight: 600;
    box-shadow: 0 10px 26px #0d6efd55;
}

.btn-secondary {
    border-radius: 14px;
    padding: 12px;
    font-weight: 600;
}
</style>
</head>

<body>

<div class="container" style="max-width: 1100px;">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold">Caixinhas & Investimentos</h2>
        <a href="home" class="btn btn-secondary">Voltar</a>
    </div>

    <!-- SALDO -->
    <div class="card-modern text-center">
        <span class="text-muted">Saldo disponível</span>
        <div class="saldo"><%= moeda.format(saldoConta) %></div>
    </div>

    <!-- CRIAR CAIXINHA -->
    <div class="card-modern">
        <h4 class="fw-bold mb-3">Criar nova caixinha</h4>

        <form action="aplicar-investimento" method="post">
            <input type="hidden" name="idTipoInvestimento" id="idTipoInvestimento">

            <div class="row g-4 mb-4">
                <% for (TipoInvestimento t : tipos) { %>
                <div class="col-md-4">
                    <div class="invest-option"
                         onclick="selecionar(<%= t.getId() %>, this)">
                        <span class="badge-bit mb-2 d-inline-block">
                            <%= t.getRentabilidadeMes() %>% a.m.
                        </span>
                        <h5 class="fw-bold"><%= t.getNome() %></h5>
                        <p class="text-muted mb-0">
                            Valor mínimo: <%= moeda.format(t.getValorMinimo()) %>
                        </p>
                        <p class="text-muted small">
                            Carência: <%= t.getCarenciaDias() %> dias
                        </p>
                    </div>
                </div>
                <% } %>
            </div>

            <div class="row g-3 align-items-end">
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Valor a aplicar</label>
                    <input type="number" step="0.01" min="10"
                           name="valor"
                           class="form-control form-control-lg"
                           required>
                </div>
                <div class="col-md-3">
                    <button class="btn btn-primary w-100">
                        Aplicar investimento
                    </button>
                </div>
            </div>
        </form>
    </div>

    <!-- MINHAS CAIXINHAS -->
    <h4 class="fw-bold mb-3">Minhas caixinhas</h4>

    <div class="row g-4">
        <% if (portfolio == null || portfolio.isEmpty()) { %>
            <p class="text-muted">Você ainda não possui investimentos ativos.</p>
        <% } else {
            for (AplicacaoInvestimento a : portfolio) { %>

        <div class="col-md-4">
            <div class="card-modern">
                <span class="badge-bit"><%= a.getStatus() %></span>

                <h5 class="fw-bold mt-3">
                    <%= a.getTipoInvestimento().getNome() %>
                </h5>

                <p class="text-muted mb-1">Valor aplicado</p>
                <h4 class="fw-bold text-primary">
                    <%= moeda.format(a.getValorAplicado()) %>
                </h4>

                <p class="small text-muted">
                    Aplicado em <%= a.getDataAplicacao() %>
                </p>

                <% if ("ATIVA".equalsIgnoreCase(a.getStatus())) { %>
                <form action="resgatar-investimento" method="post">
                    <input type="hidden" name="idAplicacao" value="<%= a.getId() %>">
                    <button class="btn btn-outline-primary w-100 mt-3">
                        Resgatar
                    </button>
                </form>
                <% } %>
            </div>
        </div>

        <% } } %>
    </div>

</div>

<script>
function selecionar(id, el) {
    document.getElementById("idTipoInvestimento").value = id;
    document.querySelectorAll(".invest-option")
        .forEach(c => c.classList.remove("active"));
    el.classList.add("active");
}
</script>

</body>
</html>
