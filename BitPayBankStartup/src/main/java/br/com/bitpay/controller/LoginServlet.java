package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.service.LoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private LoginService loginService = new LoginService(); 
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {
            Cliente clienteLogado = loginService.autenticar(email, senha);
            
            if (clienteLogado != null) {
                HttpSession session = request.getSession();
                
                session.setAttribute("cliente", clienteLogado); 
                session.setAttribute("conta", clienteLogado.getConta()); 
                
                response.sendRedirect(request.getContextPath() + "/home"); 
                return;
            }
        } catch (Exception e) {
            System.err.println("Falha no Login: " + e.getMessage());
            
            request.setAttribute("mensagem", e.getMessage());
            request.setAttribute("tipoMensagem", "danger"); 
            
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}