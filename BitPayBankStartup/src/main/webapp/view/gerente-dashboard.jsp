<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.bitpay.model.Cliente" %>
<%@ page import="br.com.bitpay.model.Gerente" %>
<%@ page import="java.util.List" %>

<%
    Object usuarioLogado = session.getAttribute("usuarioLogado");
    
    if (usuarioLogado == null || !(usuarioLogado instanceof Gerente)) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    
    Gerente gerenteLogado = (Gerente) usuarioLogado;
    String nomeGerente = gerenteLogado.getNome();

    List<Cliente> clientesPendentes = (List<Cliente>) request.getAttribute("clientesPendentes");
    if (clientesPendentes == null) {
        clientesPendentes = java.util.Collections.emptyList();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Dashboard do Gerente</title>
    <base href="<%= request.getContextPath() %>/">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</head>
<body>
    
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container">
        <a class="navbar-brand" href="gerente-dashboard">Bit Pay Administrador</a>
        <div class="collapse navbar-collapse">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <span class="navbar-text">
                Olá, <%= nomeGerente.split(" ")[0] %>
              </span>
            </li>
            <li class="nav-item ms-3">
              <a href="logout" class="btn btn-outline-light btn-sm">Sair</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <div class="container mt-5">
        
        <header class="p-4 mb-4 bg-primary text-white rounded">
            <h1><i class="bi bi-shield-lock-fill"></i> Dashboard do Gerente</h1>
            <p class="lead">Aprovação de Contas Pendentes</p>
        </header>
        
        <h2>Contas Pendentes de Aprovação (<%= clientesPendentes.size() %>)</h2>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Nº Conta</th>
                    <th>Nome Cliente</th>
                    <th>CPF</th>
                    <th>Email</th>
                    <th>Aprovar</th>
                </tr>
            </thead>
            <tbody>
                <% for (Cliente cliente : clientesPendentes) { %>
                <tr>
                    <td><%= cliente.getConta().getNumeroConta() %></td>
                    <td><%= cliente.getNome() %></td>
                    <td><%= cliente.getCpf() %></td>
                    <td><%= cliente.getEmail() %></td>
                    <td>
                        <form action="aprovar-conta" method="POST">
                            <input type="hidden" name="idConta" value="<%= cliente.getConta().getContaId() %>">
                            <button type="submit" class="btn btn-success btn-sm">Aprovar</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <% if (clientesPendentes.isEmpty()) { %>
            <div class="alert alert-info">Nenhuma conta pendente de aprovação no momento.</div>
        <% } %>
        
    </div>
	<%
	    String mensagemSucesso = (String) session.getAttribute("mensagemSucesso");
	    String mensagemErro = (String) session.getAttribute("mensagemErro");
	
	    session.removeAttribute("mensagemSucesso");
	    session.removeAttribute("mensagemErro");
	%>
	
	<div class="container mt-3">
	    <% if (mensagemSucesso != null) { %>
	        <div class="alert alert-success" role="alert">
	            <%= mensagemSucesso %>
	        </div>
	    <% } %>
	
	    <% if (mensagemErro != null) { %>
	        <div class="alert alert-danger" role="alert">
	            <%= mensagemErro %>
	        </div>
	    <% } %>
	</div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>