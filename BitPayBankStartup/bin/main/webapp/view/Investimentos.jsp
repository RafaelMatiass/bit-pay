<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.TipoInvestimento" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.math.BigDecimal" %>

<%
    String contextPath = request.getContextPath();
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    
  
    List<TipoInvestimento> tipos = (List<TipoInvestimento>) request.getAttribute("tiposInvestimento");
    String saldoConta = (String) request.getAttribute("saldoConta");
    int idConta = (Integer) request.getAttribute("idConta");
    String mensagem = (String) request.getAttribute("mensagem");
    String tipoMensagem = (String) request.getAttribute("tipoMensagem");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Investimentos</title>
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
        <h2>Fazer Aplicações em Investimentos</h2>
        
        <div class="alert alert-info">
            Seu saldo atual é: <strong><%= saldoConta %></strong>
        </div>

        <% if (mensagem != null) { %>
            <div class="alert alert-<%= tipoMensagem %>">
                <%= mensagem %>
            </div>
        <% } %>

        <div class="row g-4">
            
            <% if (tipos != null && !tipos.isEmpty()) { 
                for (TipoInvestimento tipo : tipos) {
            %>
             
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 shadow-sm">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title text-primary"><%= tipo.getNome() %></h5>
                            <hr>
                            <p class="card-text">
                                **Rentabilidade Mensal:** <%= tipo.getRentabilidadeMes().multiply(new BigDecimal(100)).toPlainString() %>% <br>
                                **Carência:** <%= tipo.getCarenciaDias() %> dias <br>
                                **Valor Mínimo:** <%= nf.format(tipo.getValorMinimo()) %>
                            </p>
                            
                   
                            <div class="mt-auto">
                                <button type="button" class="btn btn-success w-100" 
                                        data-bs-toggle="modal" data-bs-target="#aplicarModal"
                                        data-bs-id="<%= tipo.getId() %>"
                                        data-bs-nome="<%= tipo.getNome() %>"
                                        data-bs-minimo="<%= tipo.getValorMinimo() %>">
                                    Aplicar Agora
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            <% }
            } else { %>
                <div class="col-12">
                    <div class="alert alert-warning">Nenhum tipo de investimento encontrado.</div>
                </div>
            <% } %>
        </div>
    </main>


    <div class="modal fade" id="aplicarModal" tabindex="-1" aria-labelledby="aplicarModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <form action="<%= contextPath %>/aplicarInvestimento" method="POST">
              <div class="modal-header">
                <h5 class="modal-title" id="aplicarModalLabel">Aplicar em Investimento</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <input type="hidden" name="idTipoInvestimento" id="modal-idTipoInvestimento">
                <input type="hidden" name="idConta" value="<%= idConta %>"> 

                <p>Você está aplicando em: <strong id="modal-nomeProduto"></strong></p>
                <p class="text-danger small">Mínimo necessário: <span id="modal-valorMinimo"></span></p>

                <div class="mb-3">
                  <label for="valorAplicacao" class="form-label">Valor da Aplicação (R$)</label>
                  <input type="number" step="0.01" min="0.01" class="form-control" 
                         id="valorAplicacao" name="valorAplicacao" required>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="submit" class="btn btn-success">Confirmar Aplicação</button>
              </div>
          </form>
        </div>
      </div>
    </div>

 
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            var aplicarModal = document.getElementById('aplicarModal');
            aplicarModal.addEventListener('show.bs.modal', function (event) {
                var button = event.relatedTarget;
                var id = button.getAttribute('data-bs-id');
                var nome = button.getAttribute('data-bs-nome');
                var minimo = button.getAttribute('data-bs-minimo');
                
                var modalTitle = aplicarModal.querySelector('.modal-title');
                var modalBodyNome = aplicarModal.querySelector('#modal-nomeProduto');
                var modalInputId = aplicarModal.querySelector('#modal-idTipoInvestimento');
                var modalMinimo = aplicarModal.querySelector('#modal-valorMinimo');
                var modalInputValor = aplicarModal.querySelector('#valorAplicacao');
                
                modalTitle.textContent = 'Aplicar em ' + nome;
                modalBodyNome.textContent = nome;
                modalInputId.value = id;
                modalMinimo.textContent = parseFloat(minimo).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
                
            
                modalInputValor.setAttribute('min', parseFloat(minimo));
            });
        });
    </script>
</body>
</html>