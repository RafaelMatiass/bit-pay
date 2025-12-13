package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Gerente;
import br.com.bitpay.service.GerenteService;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/gerente-dashboard")
public class GerenteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GerenteService gerenteService = new GerenteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioLogado = request.getSession().getAttribute("usuarioLogado");

        // GARANTE QUE APENAS GERENTE PODE ENTRAR
        if (usuarioLogado == null || !(usuarioLogado instanceof Gerente)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try {

            // BUSCAR TODAS AS LISTAS
            List<Cliente> clientesPendentes = gerenteService.buscarPendentes();
            List<Cliente> clientesAprovados = gerenteService.buscarAprovados();
            List<Cliente> clientesRecusados = gerenteService.buscarRecusados();

            // ENVIAR AO JSP
            request.setAttribute("clientesPendentes", clientesPendentes);
            request.setAttribute("clientesAprovados", clientesAprovados);
            request.setAttribute("clientesRecusados", clientesRecusados);

            request.getRequestDispatcher("/view/gerente-dashboard.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("mensagemErro", "Erro ao carregar dashboard: " + e.getMessage());
            request.getRequestDispatcher("/view/gerente-dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int idConta = Integer.parseInt(request.getParameter("idConta"));

        try {
            if ("aprovar".equals(action)) {
                gerenteService.aprovarConta(idConta);
                request.getSession().setAttribute("mensagemSucesso", "Conta aprovada com sucesso!");
            }
            else if ("recusar".equals(action)) {
                gerenteService.recusarConta(idConta);
                request.getSession().setAttribute("mensagemErro", "Conta recusada.");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("mensagemErro", "Falha: " + e.getMessage());
        }

        // VOLTAR PARA DASHBOARD SEM NOVO POST
        response.sendRedirect("gerente-dashboard");
    }
}
