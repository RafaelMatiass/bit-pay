package br.com.bitpay.controller;

import br.com.bitpay.model.Gerente;
import br.com.bitpay.service.GerenteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/aprovar-conta") 
public class AprovarContaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
	private GerenteService gerenteService = new GerenteService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        Object usuarioLogado = session.getAttribute("usuarioLogado");
        
        if (usuarioLogado == null || !(usuarioLogado instanceof Gerente)) {

            response.sendRedirect(request.getContextPath() + "/index.jsp"); 
            return;
        }

        String idContaStr = request.getParameter("idConta");
        
        try {
            int idConta = Integer.parseInt(idContaStr);
            
            gerenteService.aprovarConta(idConta);
            
            session.setAttribute("mensagemSucesso", "Conta aprovada e ativada com sucesso.");
            
        } catch (NumberFormatException e) {
            session.setAttribute("mensagemErro", "Erro: ID de conta inválido no formulário.");
        } catch (Exception e) {
            session.setAttribute("mensagemErro", "Falha na aprovação: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/gerente-dashboard");
    }
}