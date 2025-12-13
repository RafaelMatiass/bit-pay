<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="br.com.bitpay.model.Movimentacao" %>
<%@ page import="br.com.bitpay.model.Conta" %>
<%@ page import="br.com.bitpay.model.Enums.TipoMovimento" %>
<%@ page import="java.text.NumberFormat, java.util.Locale" %>

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
    if (isFilterSubmitted == null) isFilterSubmitted = false;

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Extrato • BitPay Blue</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<style>
    :root {
        --primary:#0d6efd;
        --primary-light:#e9f1ff;
        --bg:#f5f7fc;
        --text:#1c1f27;
        --radius:18px;
    }

    body {
        background: linear-gradient(160deg,#eaf1ff,#eef4ff,#dfe9ff);
        font-family: Inter, sans-serif;
    }

    .card-filter {
        padding: 28px;
        border-radius: var(--radius);
        background: white;
        box-shadow: 0 12px 40px #0d6efd25;
        border:1px solid #dbe3ff;
    }

    .filter-title {
        font-size: 20px;
        font-weight: 700;
        color:#0a2a57;
        margin-bottom: 10px;
    }

    .btn-primary {
        background: var(--primary);
        border-radius: 14px;
        border: none;
        padding: 10px 22px;
        font-weight: 600;
        box-shadow: 0 8px 18px #0d6efd40;
    }
    .btn-primary:hover {
        transform: translateY(-2px);
        filter:brightness(1.08);
    }

    .btn-pdf {
        background: #0aa86e;
        color:white;
        border-radius: 14px;
        font-weight:600;
        padding: 10px 22px;
        border:none;
        box-shadow:0 8px 20px #0aa86e55;
    }
    .btn-pdf:hover {
        transform:translateY(-2px);
        filter:brightness(1.1);
    }

    /* ------ TABELA PREMIUM ------ */

    .table-premium {
        border-radius: var(--radius);
        overflow: hidden;
        background: white;
        box-shadow: 0 12px 36px #0d6efd22;
        border:1px solid #e3e9ff;
    }

    .table-premium thead {
        background: linear-gradient(90deg,#0d6efd,#4c8dff);
        color: white;
    }

    .table-premium tbody tr {
        transition: 0.25s;
    }
    .table-premium tbody tr:hover {
        background: #0d6efd12;
    }

    .valor-positivo {
        color: #0abf72;
        font-weight: 700;
    }
    .valor-negativo {
        color: #d63b3b;
        font-weight: 700;
    }

    .table-metadata {
        font-size: 12px;
        color:#7c8599;
        opacity:.7;
        margin-top:-4px;
    }

</style>

</head>
<body>

<div class="container py-5">

    <h2 class="fw-bold text-primary mb-1">
        Extrato da Conta
    </h2>
    <p class="text-muted">
        Conta <strong><%= conta.getNumeroConta() %></strong>
    </p>

    <% if (erro != null) { %>
        <div class="alert alert-danger">
            <%= erro %>
        </div>
    <% } %>

    <!-- ===== FILTRO ===== -->
    <div class="card-filter mt-3 mb-5">
        <div class="filter-title">Filtrar por Período</div>

        <form action="extrato" method="post" class="row g-3">
            <div class="col-md-4">
                <label class="form-label">Data inicial</label>
                <input type="date" class="form-control" name="dataInicial"
                       value="<%= dataInicial != null ? dataInicial : "" %>">
            </div>

            <div class="col-md-4">
                <label class="form-label">Data final</label>
                <input type="date" class="form-control" name="dataFinal"
                       value="<%= dataFinal != null ? dataFinal : "" %>">
            </div>

            <div class="col-md-4 d-flex gap-2 align-items-end">
                <button type="submit" name="acao" value="buscar" class="btn btn-primary w-50">
                    <i class="bi bi-search"></i> Buscar
                </button>

                <% if (extrato != null && !extrato.isEmpty()) { %>
                <button type="submit" name="acao" value="gerarPDF" class="btn btn-pdf w-50">
                    <i class="bi bi-file-earmark-pdf-fill"></i> PDF
                </button>
                <% } %>
            </div>
        </form>
    </div>

    <!-- ===== RESULTADO DO EXTRATO ===== -->
    <% if (extrato != null) { %>

        <% if (extrato.isEmpty()) { %>
            <div class="alert alert-info">Nenhuma movimentação encontrada para o período selecionado.</div>

        <% } else { %>

            <h3 class="fw-bold text-dark mb-3">
                Movimentações dos últimos 30 dias
            </h3>

            <div class="table-responsive">
                <table class="table table-premium">
                    <thead>
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
                            <td>
                                <%= mov.getDataMovimento() %>
                                <div class="table-metadata">
                                    <%= mov.getTipoMovimento().name().contains("RECEBIDA") ? "Entrada" : "" %>
                                    <%= mov.getTipoMovimento().name().contains("ENVIADA") ? "Saída" : "" %>
                                </div>
                            </td>

                            <td>
                                <%
                                    String nome = mov.getNomeClienteDestino();
                                    switch (mov.getTipoMovimento()) {
                                        case TRANSFERENCIA_ENVIADA:
                                            out.print(nome != null ? nome : "Destinatário Inválido");
                                            break;
                                        case TRANSFERENCIA_RECEBIDA:
                                            out.print(nome != null ? nome : "Remetente Inválido");
                                            break;
                                        default:
                                            out.print("Própria Conta");
                                    }
                                %>
                            </td>

                            <td><%= mov.getNumeroContaDestino() != null ? mov.getNumeroContaDestino() : "-" %></td>

                            <td><%= mov.getTipoMovimento().getDescricao() %></td>

                            <td class="text-end <%= mov.getTipoMovimento().name().contains("ENVIADA") || mov.getTipoMovimento() == TipoMovimento.SAQUE ? "valor-negativo" : "valor-positivo" %>">
                                <%= nf.format(mov.getValor()) %>
                            </td>
                        </tr>

                    <% } %>
                    </tbody>
                </table>
            </div>

        <% } %>

    <% } %>

    <a href="home" class="btn btn-secondary mt-4">Voltar para a Home</a>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
