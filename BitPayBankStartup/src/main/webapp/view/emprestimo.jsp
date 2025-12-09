<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="br.com.bitpay.model.Conta"%>
<%@ page import="br.com.bitpay.model.ParcelaEmprestimo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Date" %>

<%! 
    private static final int SCALE = 2;
%>
<%
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    NumberFormat decimalFormat = NumberFormat.getNumberInstance(Locale.of("pt", "BR"));
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

    if (simulacao != null && !simulacao.isEmpty()) {
        for (ParcelaEmprestimo p : simulacao) {
            totalAmortizacao = totalAmortizacao.add(p.getValorAmortizacao().setScale(SCALE, java.math.RoundingMode.HALF_UP));
            totalJuros = totalJuros.add(p.getValorJuros().setScale(SCALE, java.math.RoundingMode.HALF_UP));
            totalParcela = totalParcela.add(p.getValorParcela().setScale(SCALE, java.math.RoundingMode.HALF_UP));
        }
       
        totalJuros = totalParcela.subtract(saldoDevedorEfetivo);
    }
    String taxaMensalExibicao = "";
    if (taxaJuros != null) {
        taxaMensalExibicao = decimalFormat.format(taxaJuros);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Simular Empréstimo</title>
    <base href="<%= request.getContextPath() %>/">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</head>
<body>
    
    <div class="container mt-5">
        <h1>Simulador de Empréstimo (SAC)</h1>
        
        <% 
            if (mensagemErro != null && !mensagemErro.isEmpty()) {
        %>
            <div class="alert alert-danger mt-3" role="alert"><%= mensagemErro %></div>
        <% 
            } 
        %>

        <form action="emprestimo" method="POST" class="p-4 border rounded shadow-sm">
            <h3 class="mb-4">Calcular Tabela</h3>
            <div class="row">
                <div class="col-md-3 mb-3">
                    <label for="entrada" class="form-label">Entrada (R$)</label>
                    <input type="number" step="0.01" min="0.00" class="form-control" id="entrada" name="entrada" 
                           placeholder="Ex: 5000.00" value="0.00" required>
                </div>
                <div class="col-md-3 mb-3">
                    <label for="valor" class="form-label">Valor Total (R$)</label>
                    <input type="number" step="0.01" min="1000" class="form-control" id="valor" name="valor" 
                           placeholder="Ex: 50000.00" required>
                </div>
                <div class="col-md-3 mb-3">
                    <label for="parcelas" class="form-label">Número de Parcelas</label>
                    <input type="number" min="2" max="60" class="form-control" id="parcelas" name="parcelas" 
                           placeholder="Ex: 12" required>
                </div>
                <div class="col-md-3 mb-3">
                    <label for="taxaJuros" class="form-label">Taxa de Juros Mensal (% a.m.)</label>
                    <input type="number" step="0.0001" min="0.0001" class="form-control" id="taxaJuros" name="taxaJuros" 
                           placeholder="Ex: 0.5" required>
                    <small class="form-text text-muted">A taxa deve ser em porcentagem (ex: 12.0 para 12% a.m.).</small>
                </div>
            </div>
            
            <button type="submit" class="btn btn-primary mt-3">Simular</button>
        </form>

        <% 
            if (simulacao != null && !simulacao.isEmpty()) {
        %>
            <h2 class="mt-5">Resultado da Simulação</h2>
            <hr>

            <div class="row mb-4">
                <div class="col-md-6">
                    <table class="table table-sm table-borderless">
                        <thead class="table-light">
                            <tr><th colspan="2" class="text-center">Meu Cálculo - Resumo</th></tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><strong>Valor de Financiamento</strong></td>
                                <td class="text-end"><%= currencyFormat.format(valorTotal) %></td>
                            </tr>
                            <tr>
                                <td><strong>Entrada</strong></td>
                                <td class="text-end"><%= currencyFormat.format(valorEntrada) %></td>
                            </tr>
                            <tr class="table-primary">
                                <td><strong>Saldo Devedor Efetivo</strong></td>
                                <td class="text-end"><strong><%= currencyFormat.format(saldoDevedorEfetivo) %></strong></td>
                            </tr>
                            <tr><td colspan="2"><hr class="my-1"></td></tr>
                            <tr>
                                <td><strong>Total a ser Pago (C/ Juros)</strong></td>
                                <td class="text-end"><%= currencyFormat.format(totalParcela) %></td>
                            </tr>
                            <tr>
                                <td><strong>Total de Juros</strong></td>
                                <td class="text-end"><%= currencyFormat.format(totalJuros) %></td>
                            </tr>
                            <tr><td colspan="2"><hr class="my-1"></td></tr>
                            <tr>
                                <td><strong>Taxa de Juros (a.m.)</strong></td>
                                <td class="text-end"><%= taxaMensalExibicao %>%</td>
                            </tr>
                            <tr>
                                <td><strong>Tipo de Financiamento</strong></td>
                                <td class="text-end">SAC (Sistema de Amortização Constante)</td>
                            </tr>
                            <tr>
                                <td><strong>Período em</strong></td>
                                <td class="text-end"><%= numeroParcelas %> meses</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <h3 class="mb-4">Tabela Detalhada de Amortização</h3>

            <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead class="table-dark">
                        <tr>
                            <th>Nº</th>
                            <th>Vencimento</th>
                            <th>Amortização</th>
                            <th>Juros</th>
                            <th>Valor Parcela</th>
                            <th>Saldo Devedor</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (ParcelaEmprestimo parcela : simulacao) {
                                Date dataVencimento = Date.from(parcela.getDataVencimento().atStartOfDay(ZoneId.systemDefault()).toInstant());
                        %>
                            <tr>
                                <td><%= parcela.getNumeroParcela() %></td>
                                
                                <td><%= dateFormat.format(dataVencimento) %></td>
                                
                                <td><%= currencyFormat.format(parcela.getValorAmortizacao()) %></td>
                                <td><%= currencyFormat.format(parcela.getValorJuros()) %></td>
                                <td><%= currencyFormat.format(parcela.getValorParcela()) %></td>
                                <td><%= currencyFormat.format(parcela.getSaldoDevedorAtual()) %></td>
                            </tr>
                        <%
                            }
                        %>
                    </tbody>
                    <tfoot class="table-info">
                        <tr>
                            <td colspan="2"><strong>Totais</strong></td>
                            <td><strong><%= currencyFormat.format(totalAmortizacao) %></strong></td>
                            <td><strong><%= currencyFormat.format(totalJuros) %></strong></td>
                            <td><strong><%= currencyFormat.format(totalParcela) %></strong></td>
                            <td>-</td>
                        </tr>
                    </tfoot>
                </table>
            </div>
            
            <form action="contratar-emprestimo" method="POST" class="mt-4">
                <input type="hidden" name="valorTotal" value="<%= valorTotal %>">
                <input type="hidden" name="valorEntrada" value="<%= valorEntrada %>">
                <input type="hidden" name="taxaJuros" value="<%= taxaJuros %>">
                <input type="hidden" name="parcelas" value="<%= numeroParcelas %>">
                <button type="submit" class="btn btn-success">Contratar Agora (<%= currencyFormat.format(totalParcela) %> Total)</button>
            </form>

        <% 
            } 
        %>
    </div>
</body>
</html>