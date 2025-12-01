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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	Object usuarioLogado = request.getSession().getAttribute("usuarioLogado");
        
        if (usuarioLogado == null || !(usuarioLogado instanceof Gerente)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try {
            List<Cliente> clientesPendentes = gerenteService.buscarPendentes();
            
            request.setAttribute("clientesPendentes", clientesPendentes);
            request.getRequestDispatcher("/view/gerente-dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("mensagem", "Erro ao carregar lista de aprovação: " + e.getMessage());
            request.setAttribute("tipoMensagem", "danger");
            request.getRequestDispatcher("/view/home.jsp").forward(request, response);
        }
    }
}