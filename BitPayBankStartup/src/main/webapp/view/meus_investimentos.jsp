<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.AplicacaoInvestimento" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<%@ page import="java.math.BigDecimal" %>

<%
    String contextPath = request.getContextPath();
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    
    List<AplicacaoInvestimento> portfolio = (List<AplicacaoInvestimento>) request.getAttribute("portfolio");
    int idConta = (Integer) request.getAttribute("idConta");
    
    String mensagem = (String) request.getAttribute("mensagem");
    String tipoMensagem = (String) request.getAttribute("tipoMensagem");
    
    LocalDate hoje = LocalDate.now();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Meu Portfólio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    
    <!-- Navbar -->
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
        <h2>Meu Portfólio de Investimentos</h2>
        <p class="text-muted">Apenas aplicações ATIVAS são exibidas.</p>

        <% if (mensagem != null) { %>
            <div class="alert alert-<%= tipoMensagem %>">
                <%= mensagem %>
            </div>
        <% } %>

        <% if (portfolio == null || portfolio.isEmpty()) { %>
            <div class="alert alert-warning">Você não possui aplicações de investimento ativas.</div>
        <% } else { %>
            <div class="table-responsive">
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>Produto</th>
                            <th>Aplicação</th>
                            <th>Carência</th>
                            <th>Situação</th>
                            <th>Ação</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (AplicacaoInvestimento app : portfolio) { 
                            LocalDate dataAplicacao = app.getDataAplicacao();
                            int carenciaDias = app.getTipoInvestimento().getCarenciaDias();
                            LocalDate dataLiberacao = dataAplicacao.plusDays(carenciaDias);
                            
                            // Calcula o valor atualizado para exibição
                            long mesesPassados = ChronoUnit.MONTHS.between(dataAplicacao, hoje);
                            BigDecimal valorAtual = app.getValorAplicado();
                            BigDecimal taxaMensal = app.getTipoInvestimento().getRentabilidadeMes();
                            
                            for (int i = 0; i < mesesPassados; i++) {
                                BigDecimal rendimento = valorAtual.multiply(taxaMensal).setScale(2, BigDecimal.ROUND_HALF_UP);
                                valorAtual = valorAtual.add(rendimento);
                            }
                            
                            boolean podeResgatar = hoje.isEqual(dataLiberacao) || hoje.isAfter(dataLiberacao);
                            String classeSituacao = podeResgatar ? "text-success" : "text-warning";
                        %>
                        <tr id="app-<%= app.getId() %>">
                            <td>
                                <strong><%= app.getTipoInvestimento().getNome() %></strong><br>
                                <span class="text-muted small">Aplicado: <%= nf.format(app.getValorAplicado()) %></span>
                            </td>
                            <td>
                                <%= dataAplicacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %>
                            </td>
                            <td>
                                <%= carenciaDias %> dias
                            </td>
                            <td>
                                <strong class="<%= classeSituacao %>">
                                    <%= podeResgatar ? "Resgate Imediato" : "Aguardando" %>
                                </strong>
                                <br>
                                <span class="small">Liberação: <%= dataLiberacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></span>
                            </td>
                            <td>
                                <div class="d-flex flex-column align-items-center">
                                    <span class="fw-bold mb-1">Valor Estimado: <%= nf.format(valorAtual) %></span>
                                    
                                    <% if (podeResgatar) { %>
                                        <button type="button" class="btn btn-sm btn-primary" 
                                                data-bs-toggle="modal" data-bs-target="#resgateModal"
                                                data-bs-id="<%= app.getId() %>"
                                                data-bs-valor="<%= nf.format(valorAtual) %>"
                                                data-bs-nome="<%= app.getTipoInvestimento().getNome() %>">
                                            Resgatar
                                        </button>
                                    <% } else { %>
                                        <button type="button" class="btn btn-sm btn-outline-secondary" disabled>
                                            Em Carência
                                        </button>
                                    <% } %>
                                </div>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </main>

    <!-- Modal de Confirmação de Resgate -->
    <div class="modal fade" id="resgateModal" tabindex="-1" aria-labelledby="resgateModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <form action="<%= contextPath %>/meus-investimentos" method="POST">
              <div class="modal-header">
                <h5 class="modal-title" id="resgateModalLabel">Confirmar Resgate</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <input type="hidden" name="idAplicacao" id="modal-idAplicacao">
                <input type="hidden" name="idConta" value="<%= idConta %>"> 

                <p>Confirma o resgate do investimento <strong id="modal-nomeResgate"></strong>?</p>
                <p class="fw-bold">Valor Total Estimado: <span id="modal-valorResgate"></span></p>
                <p class="text-danger small">Este valor será creditado imediatamente na sua conta corrente.</p>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="submit" class="btn btn-primary">Confirmar Resgate</button>
              </div>
          </form>
        </div>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            var resgateModal = document.getElementById('resgateModal');
            resgateModal.addEventListener('show.bs.modal', function (event) {
                var button = event.relatedTarget;
                var id = button.getAttribute('data-bs-id');
                var nome = button.getAttribute('data-bs-nome');
                var valor = button.getAttribute('data-bs-valor');
                
                resgateModal.querySelector('#modal-idAplicacao').value = id;
                resgateModal.querySelector('#modal-nomeResgate').textContent = nome;
                resgateModal.querySelector('#modal-valorResgate').textContent = valor;
            });
        });
    </script>
</body>
</html>